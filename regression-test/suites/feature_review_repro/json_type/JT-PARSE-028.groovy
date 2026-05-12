// JT-PARSE-028: emoji key
suite("repro_jt_parse_028") {
    def r = sql "SELECT jsonb_parse('{\"🎉\":1}')"
    assertNotNull(r[0][0], "JT-PARSE-028: emoji key should parse; observed=${r}")
}
