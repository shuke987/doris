// JT-PARSE-031: key 含 '['
suite("repro_jt_parse_031") {
    def r = sql "SELECT jsonb_parse('{\"a[\":1}')"
    assertNotNull(r[0][0], "JT-PARSE-031; observed=${r}")
}
