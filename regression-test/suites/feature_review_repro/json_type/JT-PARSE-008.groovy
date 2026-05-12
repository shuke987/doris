// JT-PARSE-008: scalar double
suite("repro_jt_parse_008") {
    def r = sql "SELECT jsonb_parse('3.14')"
    String val = r[0][0].toString()
    assertTrue(val.startsWith("3.14"), "JT-PARSE-008: scalar double; observed=${r}")
}
