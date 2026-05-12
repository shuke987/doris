// JT-PARSE-003: scalar string
suite("repro_jt_parse_003") {
    def r = sql "SELECT jsonb_parse('\"hello\"')"
    assertEquals("\"hello\"", r[0][0].toString(),
        "JT-PARSE-003: scalar string; observed=${r}")
}
