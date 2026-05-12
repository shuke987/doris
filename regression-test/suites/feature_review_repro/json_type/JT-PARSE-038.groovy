// JT-PARSE-038: Unicode surrogate pair
suite("repro_jt_parse_038") {
    def r = sql "SELECT jsonb_parse('{\"a\":\"😀\"}')"
    assertNotNull(r[0][0], "JT-PARSE-038: emoji value; observed=${r}")
}
