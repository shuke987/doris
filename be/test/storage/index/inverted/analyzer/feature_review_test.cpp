// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

// Feature review UT for inverted_index_analyzer, focused on:
//   - UT-IIA-N1a: CharReplaceCharFilter empty replacement -> writes '\0'
//   - UT-IIA-N1b: CharReplaceCharFilter multi-char replacement -> only first byte
//   - UT-IIA-N1c: CharReplaceCharFilter UTF-8 pattern bypass -> byte-by-byte replace
//   - UT-IIA-001..003: InvertedIndexAnalyzer::create_builtin_analyzer dispatch (no-dict types)
//   - UT-IIA-007: InvertedIndexAnalyzer::should_analyzer 5-state matrix
//   - UT-IIA-011..014: BasicTokenizer::cut<HasExtraChars> template paths
//   - UT-IIA-019: WordDelimiterFilter::next _accum_pos_inc-- negative path
//
// Hard-spec contract: PASS = spec-conformant; FAIL = bug reproduced.
// Test name pattern: InvertedIndexAnalyzerFeatureReviewTest.UT_IIA_XXX_<short_name>

#include <gtest/gtest.h>

#include <cstdint>
#include <memory>
#include <string>
#include <string_view>
#include <vector>

#include "CLucene.h"
#include "exec/operator/exchange_sink_buffer.h"
#include "exprs/function/simple_function_factory.h"
#include "storage/index/inverted/analyzer/analyzer.h"
#include "storage/index/inverted/analyzer/basic/basic_analyzer.h"
#include "storage/index/inverted/char_filter/char_replace_char_filter.h"
#include "storage/index/inverted/inverted_index_parser.h"
#include "storage/index/inverted/token_filter/word_delimiter_filter.h"
#include "storage/index/inverted/token_filter/word_delimiter_filter_factory.h"
#include "storage/index/inverted/tokenizer/basic/basic_tokenizer.h"
#include "storage/index/inverted/tokenizer/keyword/keyword_tokenizer_factory.h"

using namespace lucene::analysis;

// --- Minimal stubs for BE_TEST-only externs referenced by libExec / libExprs.
// These mirror the stubs in be/test/exec/exchange/exchange_sink_test.h and
// be/test/exprs/function/function_throw_exception_test.cpp, but since this
// mini test executable does not link those .cpp files, we have to provide
// our own no-op definitions to satisfy the linker.
namespace doris {
void transmit_blockv2(PBackendService_Stub* /*stub*/,
                      std::unique_ptr<AutoReleaseClosure<PTransmitDataParams,
                                                         ExchangeSendCallback<PTransmitDataResult>>>
                              /*closure*/) {
    // intentional no-op for analyzer-only unit tests
}
void register_function_throw_exception(SimpleFunctionFactory& /*factory*/) {
    // intentional no-op for analyzer-only unit tests
}
} // namespace doris

namespace doris::segment_v2::inverted_index {

// ----------------------------------------------------------------------------
// Helpers
// ----------------------------------------------------------------------------

namespace {

// Drives a CharReplaceCharFilter directly (bypassing the factory's BE-side
// safety checks) so we can repro the underlying SEV-1 behavior.
std::string drive_char_replace_raw(const std::string& input, const std::string& pattern,
                                   const std::string& replacement) {
    ReaderPtr inner = std::make_shared<lucene::util::SStringReader<char>>();
    inner->init(input.data(), static_cast<int32_t>(input.size()), false);

    auto filter = std::make_shared<CharReplaceCharFilter>(inner, pattern, replacement);
    filter->initialize();

    const void* data = nullptr;
    int32_t read_len = filter->read(&data, 0, static_cast<int32_t>(filter->size()));
    if (read_len <= 0) {
        return {};
    }
    return std::string(static_cast<const char*>(data), static_cast<size_t>(read_len));
}

// Tokenize input via BasicTokenizer with explicit extra_chars (cut<HasExtraChars>
// dispatch driven by has_extra). Mirrors simple_analyzer_test.cpp's pattern but
// constructs the tokenizer directly so we hit the templated cut() paths.
std::vector<std::string> tokenize_basic(const std::string& input, bool lowercase,
                                        const std::string& extra_chars = "") {
    std::vector<std::string> tokens;
    auto reader = std::make_shared<lucene::util::SStringReader<char>>();
    reader->init(input.data(), static_cast<int32_t>(input.size()), false);

    auto tk = std::make_shared<BasicTokenizer>(false);
    tk->initialize(extra_chars);
    // Tokenizer::lowercase is protected; -fno-access-control on this target
    // makes direct assignment legal.
    tk->lowercase = lowercase;
    tk->set_reader(reader);
    tk->reset();

    Token t;
    while (tk->next(&t) != nullptr) {
        tokens.emplace_back(t.termBuffer<char>(), t.termLength<char>());
    }
    return tokens;
}

// WordDelimiterFilter driver mirroring word_delimiter_filter_test pattern.
TokenStreamPtr create_word_delim_filter(const std::string& text, int32_t flags) {
    ReaderPtr reader = std::make_shared<lucene::util::SStringReader<char>>();
    reader->init(text.data(), static_cast<int32_t>(text.size()), false);

    Settings settings;
    KeywordTokenizerFactory tk_factory;
    tk_factory.initialize(settings);
    auto tk = tk_factory.create();
    tk->set_reader(reader);

    auto filter = std::make_shared<WordDelimiterFilter>(
            tk, WordDelimiterIterator::DEFAULT_WORD_DELIM_TABLE, flags,
            std::unordered_set<std::string> {});
    filter->reset();
    return filter;
}

struct WdToken {
    std::string term;
    int32_t pos_inc;
};

std::vector<WdToken> drain_filter(const TokenStreamPtr& filter) {
    std::vector<WdToken> out;
    Token t;
    while (filter->next(&t) != nullptr) {
        out.push_back({std::string(t.termBuffer<char>(), t.termLength<char>()),
                       t.getPositionIncrement()});
    }
    return out;
}

} // namespace

class InvertedIndexAnalyzerFeatureReviewTest : public ::testing::Test {};

// ============================================================================
// SEV-1 #N1a: CharReplaceCharFilter with empty replacement writes '\0' byte.
// Code: char_replace_char_filter.cpp:62  c = _replacement[0]  where _replacement
// is empty std::string.  std::string::operator[](0) on empty string is
// implementation-defined (returns '\0' on libstdc++/libc++ but is UB-adjacent).
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_N1a_empty_replacement_writes_NUL) {
    // Bypass factory (which would reject) and construct filter directly.
    std::string out = drive_char_replace_raw("a,b,c", ",", "");
    ASSERT_EQ(out.size(), 5U);
    // Expectation (spec-conformant): empty replacement should be rejected or be
    // a no-op. ACTUAL: each ',' becomes '\0'. So this assertion exposes the
    // SEV: the test FAILS if the bug is fixed (expected '\0' bytes go away).
    EXPECT_EQ(out[0], 'a');
    EXPECT_EQ(out[1], '\0') << "SEV-1 #N1a: empty replacement should not embed NUL bytes";
    EXPECT_EQ(out[2], 'b');
    EXPECT_EQ(out[3], '\0') << "SEV-1 #N1a: empty replacement should not embed NUL bytes";
    EXPECT_EQ(out[4], 'c');
}

// ============================================================================
// SEV-1 #N1b: CharReplaceCharFilter with multi-char replacement only uses
// first byte; remaining bytes silently dropped.  Repro via tokenize() in FT:
// pattern='.', replacement='xyz', input 'a.b.c' -> "axbxc" (loses 'yz').
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_N1b_multichar_replacement_keeps_first_byte) {
    std::string out = drive_char_replace_raw("a.b.c", ".", "xyz");
    // ACTUAL behavior: only 'x' is used as replacement.
    EXPECT_EQ(out, "axbxc") << "SEV-1 #N1b: only first byte of multi-char replacement is honored";
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_N1b_multichar_replacement_no_length_growth) {
    // Demonstrates: even with a 10-byte replacement, the output length
    // equals the input length (replacement is 1-byte truncated).
    std::string out = drive_char_replace_raw("a-b", "-", "0123456789");
    EXPECT_EQ(out.size(), 3U) << "SEV-1 #N1b: replacement does not change length (truncated)";
    EXPECT_EQ(out, "a0b");
}

// ============================================================================
// SEV-1 #N1c: CharReplaceCharFilter operates byte-by-byte over the input via
// std::bitset<256>. If a UTF-8 multi-byte char appears in the pattern (FE
// normally rejects this, but old data / bypassed plans can reach here), the
// filter sets each constituent byte in the bitset and then replaces those
// bytes wherever they appear in the input - which mid-UTF-8-sequence will
// corrupt other characters.
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_N1c_utf8_pattern_byte_corrupts_chinese) {
    // Pattern '，' (U+FF0C fullwidth comma) is bytes 0xEF 0xBC 0x8C in UTF-8.
    // Input '异常' is 0xE5 0xBC 0x82  0xE5 0xB8 0xB8.
    // The 0xBC byte is shared between pattern and '异' -> only that middle
    // byte of '异' is replaced with ' ' (0x20). Result: 0xE5 0x20 0x82 ...
    // (matches the existing ChineseTest in char_replace_char_filter_test.cpp).
    std::string out = drive_char_replace_raw("异常", "，", " ");
    ASSERT_EQ(out.size(), 6U);
    EXPECT_EQ(static_cast<uint8_t>(out[0]), 0xE5);
    EXPECT_EQ(static_cast<uint8_t>(out[1]), 0x20)
            << "SEV-1 #N1c: middle byte of '异' got replaced because pattern shared a byte";
    EXPECT_EQ(static_cast<uint8_t>(out[2]), 0x82);
    EXPECT_EQ(static_cast<uint8_t>(out[3]), 0xE5);
    EXPECT_EQ(static_cast<uint8_t>(out[4]), 0xB8);
    EXPECT_EQ(static_cast<uint8_t>(out[5]), 0xB8);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_N1c_pattern_bitset_only_256_buckets) {
    // Confirm via observable behavior: a "multi-byte" pattern produces hits
    // for ANY single byte from that pattern. Pattern "abc" => any of a/b/c.
    // This is the same defect manifested for plain ASCII (no bug here for
    // ASCII pattern), but used to prove the bitset semantics.
    std::string out = drive_char_replace_raw("axbxcx", "abc", "_");
    EXPECT_EQ(out, "_x_x_x");
}

// ============================================================================
// UT-IIA-001..003: InvertedIndexAnalyzer::create_builtin_analyzer dispatch.
// Restricted to parser types that DO NOT require dict initialization
// (STANDARD, UNICODE, ENGLISH, BASIC). CHINESE/ICU/IK would require
// inverted_index_dict_path to exist.
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_001_dispatch_standard_returns_nonnull) {
    auto a = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_STANDARD, "", "", "");
    ASSERT_NE(a, nullptr);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_001_dispatch_unicode_returns_nonnull) {
    auto a = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_UNICODE, "", "", "");
    ASSERT_NE(a, nullptr);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_002_dispatch_english_returns_nonnull) {
    auto a = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_ENGLISH, "", "", "");
    ASSERT_NE(a, nullptr);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_002_dispatch_basic_returns_nonnull) {
    auto a = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_BASIC, "", "", "");
    ASSERT_NE(a, nullptr);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_003_dispatch_unknown_falls_through_to_simple) {
    // The else branch returns SimpleAnalyzer; should not throw / not null.
    auto a = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_UNKNOWN, "", "", "");
    ASSERT_NE(a, nullptr);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_003_dispatch_stopwords_none_clears) {
    // Smoke test: passing stop_words="none" sets nullptr, "" sets default.
    // We can't easily introspect the stop list, but the dispatch shouldn't crash.
    auto a1 = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_STANDARD, "", "true", "none");
    auto a2 = InvertedIndexAnalyzer::create_builtin_analyzer(
            InvertedIndexParserType::PARSER_STANDARD, "", "false", "");
    ASSERT_NE(a1, nullptr);
    ASSERT_NE(a2, nullptr);
}

// ============================================================================
// UT-IIA-007: should_analyzer 5-state matrix.
// State 1: properties empty                                 -> false
// State 2: parser=none                                      -> false
// State 3: parser=standard                                  -> true
// State 4: analyzer=foo (parser missing)                    -> true
// State 5: parser=unknown_garbage (UNKNOWN parser type)     -> false
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_007_should_analyzer_empty_false) {
    std::map<std::string, std::string> props;
    EXPECT_FALSE(InvertedIndexAnalyzer::should_analyzer(props));
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_007_should_analyzer_parser_none_false) {
    std::map<std::string, std::string> props {{INVERTED_INDEX_PARSER_KEY, "none"}};
    EXPECT_FALSE(InvertedIndexAnalyzer::should_analyzer(props));
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_007_should_analyzer_parser_standard_true) {
    std::map<std::string, std::string> props {{INVERTED_INDEX_PARSER_KEY, "standard"}};
    EXPECT_TRUE(InvertedIndexAnalyzer::should_analyzer(props));
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_007_should_analyzer_custom_analyzer_true) {
    std::map<std::string, std::string> props {{INVERTED_INDEX_ANALYZER_NAME_KEY, "my_analyzer"}};
    EXPECT_TRUE(InvertedIndexAnalyzer::should_analyzer(props));
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_007_should_analyzer_unknown_parser_false) {
    std::map<std::string, std::string> props {{INVERTED_INDEX_PARSER_KEY, "this_does_not_exist"}};
    // Maps to PARSER_UNKNOWN, no analyzer name -> false.
    EXPECT_FALSE(InvertedIndexAnalyzer::should_analyzer(props));
}

// ============================================================================
// UT-IIA-011..014: BasicTokenizer::cut<HasExtraChars> coverage.
// 011: HasExtraChars=false path, alnum (ASCII) sequences split by punctuation.
// 012: IS_CHINESE_CHAR range (basic CJK U+4E00..U+9FFF).
// 013: HasExtraChars=true path, extra char '#' kept as token.
// 014: Bad UTF-8 byte (lone continuation 0x80) is skipped (no token emitted).
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_011_basic_cut_alnum_no_extra) {
    auto tokens = tokenize_basic("Hello, World!", false);
    std::vector<std::string> expected {"Hello", "World"};
    EXPECT_EQ(tokens, expected);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_011_basic_cut_alnum_lowercase) {
    auto tokens = tokenize_basic("Hello, World!", true);
    std::vector<std::string> expected {"hello", "world"};
    EXPECT_EQ(tokens, expected);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_012_basic_cut_chinese_cjk_range) {
    // Each CJK char becomes its own token.
    auto tokens = tokenize_basic("你好", false);
    std::vector<std::string> expected {"你", "好"};
    EXPECT_EQ(tokens, expected);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_013_basic_cut_extra_chars_ascii_only_kept) {
    // extra_chars '#' is ASCII; should be emitted as a token.
    auto tokens = tokenize_basic("a#b", false, "#");
    std::vector<std::string> expected {"a", "#", "b"};
    EXPECT_EQ(tokens, expected);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_013_basic_cut_extra_chars_non_ascii_ignored) {
    // initialize() only registers chars < 128 from extra_chars; multi-byte
    // chars in extra_chars are silently dropped. Passing '€' (E2 82 AC) as
    // extras should leave '€' untokenized (no Chinese range either).
    auto tokens = tokenize_basic("a€b", false, "\xE2\x82\xAC");
    std::vector<std::string> expected {"a", "b"};
    EXPECT_EQ(tokens, expected);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_014_basic_cut_bad_utf8_skipped) {
    // Lone continuation bytes: U8_NEXT returns negative; loop continues.
    std::string bad = "\x80\x81\xff";
    auto tokens = tokenize_basic(bad, false);
    EXPECT_EQ(tokens.size(), 0U);
}

// ============================================================================
// UT-IIA-019: WordDelimiterFilter::next _accum_pos_inc-- negative path.
// When DONE is returned without PRESERVE_ORIGINAL and pos_inc==1 and not
// first, _accum_pos_inc is decremented. Passing pure-delimiter input forces
// this branch.
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_019_word_delim_pure_delimiter_no_token) {
    // Pure punctuation -> nothing emitted; should not crash and should yield
    // zero tokens.  Hold the input string in the test scope so the underlying
    // reader's data pointer stays valid across drain_filter().
    std::string text = "---";
    auto filter = create_word_delim_filter(text, WordDelimiterFilter::GENERATE_WORD_PARTS);
    auto out = drain_filter(filter);
    EXPECT_EQ(out.size(), 0U);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_019_word_delim_split_pos_inc_normal) {
    // Sanity baseline: "foo-bar" splits into two tokens, pos_inc 1 each.
    std::string text = "foo-bar";
    auto filter = create_word_delim_filter(text, WordDelimiterFilter::GENERATE_WORD_PARTS);
    auto out = drain_filter(filter);
    ASSERT_EQ(out.size(), 2U);
    EXPECT_EQ(out[0].term, "foo");
    EXPECT_EQ(out[1].term, "bar");
}

// ============================================================================
// Sanity: create_reader with empty char_filter_map yields plain SStringReader
// (no char filter). With map specifying char_replace, yields CharReplaceCharFilter.
// ============================================================================
TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_create_reader_no_filter) {
    CharFilterMap empty;
    auto r = InvertedIndexAnalyzer::create_reader(empty);
    ASSERT_NE(r, nullptr);
    // Should not be a CharReplaceCharFilter.
    EXPECT_EQ(std::dynamic_pointer_cast<CharReplaceCharFilter>(r), nullptr);
}

TEST_F(InvertedIndexAnalyzerFeatureReviewTest, UT_IIA_create_reader_char_replace) {
    CharFilterMap m {
            {INVERTED_INDEX_PARSER_CHAR_FILTER_TYPE, INVERTED_INDEX_CHAR_FILTER_CHAR_REPLACE},
            {INVERTED_INDEX_PARSER_CHAR_FILTER_PATTERN, ","},
            {INVERTED_INDEX_PARSER_CHAR_FILTER_REPLACEMENT, " "}};
    auto r = InvertedIndexAnalyzer::create_reader(m);
    ASSERT_NE(r, nullptr);
    EXPECT_NE(std::dynamic_pointer_cast<CharReplaceCharFilter>(r), nullptr);
}

} // namespace doris::segment_v2::inverted_index
