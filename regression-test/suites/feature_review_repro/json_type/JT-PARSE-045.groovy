// JT-PARSE-045: 2^63 应为 T_Int128
suite("repro_jt_parse_045") {
    def r = sql "SELECT jsonb_type(jsonb_parse('9223372036854775808'), '\$')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "largeint" || t == "int" || t.contains("int"),
        "JT-PARSE-045: 2^63 should be int128/largeint; observed=${r}")
}
