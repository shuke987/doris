// JT-PARSE-030: key 含 '$'
suite("repro_jt_parse_030") {
    def r = sql "SELECT jsonb_parse('{\"\$key\":1}')"
    assertNotNull(r[0][0], "JT-PARSE-030; observed=${r}")
}
