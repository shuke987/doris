// JT-PARSE-007: scalar null
suite("repro_jt_parse_007") {
    def r = sql "SELECT jsonb_parse('null')"
    assertEquals("null", r[0][0].toString(),
        "JT-PARSE-007: scalar null; observed=${r}")
}
