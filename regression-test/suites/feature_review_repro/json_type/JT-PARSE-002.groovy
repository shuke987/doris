// JT-PARSE-002: jsonb_parse 标准 array
suite("repro_jt_parse_002") {
    def r = sql "SELECT jsonb_parse('[1,2,3]')"
    assertEquals("[1,2,3]", r[0][0].toString(),
        "JT-PARSE-002: standard array; observed=${r}")
}
