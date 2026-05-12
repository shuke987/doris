// JT-PARSE-004: scalar int
suite("repro_jt_parse_004") {
    def r = sql "SELECT jsonb_parse('42')"
    assertEquals("42", r[0][0].toString(),
        "JT-PARSE-004: scalar int; observed=${r}")
}
