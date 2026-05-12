// JT-PARSE-010: empty array
suite("repro_jt_parse_010") {
    def r = sql "SELECT jsonb_parse('[]')"
    assertEquals("[]", r[0][0].toString(),
        "JT-PARSE-010: empty array; observed=${r}")
}
