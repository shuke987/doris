// JT-PARSE-016: NULL 输入 (FOLLOW_INPUT)
suite("repro_jt_parse_016") {
    def r = sql "SELECT jsonb_parse(NULL)"
    assertEquals(null, r[0][0],
        "JT-PARSE-016: NULL input → NULL; observed=${r}")
}
