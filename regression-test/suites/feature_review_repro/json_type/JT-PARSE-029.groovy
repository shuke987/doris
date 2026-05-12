// JT-PARSE-029: key 含 '.'
suite("repro_jt_parse_029") {
    def r = sql "SELECT jsonb_parse('{\"a.b\":1}')"
    assertNotNull(r[0][0], "JT-PARSE-029; observed=${r}")
}
