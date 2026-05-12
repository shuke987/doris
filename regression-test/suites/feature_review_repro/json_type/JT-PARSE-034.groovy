// JT-PARSE-034: key 是空字符串
suite("repro_jt_parse_034") {
    def r = sql "SELECT jsonb_parse('{\"\":1}')"
    assertNotNull(r[0][0], "JT-PARSE-034; observed=${r}")
}
