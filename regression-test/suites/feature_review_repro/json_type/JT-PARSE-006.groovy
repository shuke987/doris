// JT-PARSE-006: scalar bool false
suite("repro_jt_parse_006") {
    def r = sql "SELECT jsonb_parse('false')"
    assertEquals("false", r[0][0].toString(),
        "JT-PARSE-006: scalar bool false; observed=${r}")
}
