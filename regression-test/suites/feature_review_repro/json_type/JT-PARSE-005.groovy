// JT-PARSE-005: scalar bool true
suite("repro_jt_parse_005") {
    def r = sql "SELECT jsonb_parse('true')"
    assertEquals("true", r[0][0].toString(),
        "JT-PARSE-005: scalar bool true; observed=${r}")
}
