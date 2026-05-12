// JT-PARSE-082: 2 参 default 是 NULL
suite("repro_jt_parse_082") {
    def r = sql "SELECT jsonb_parse_error_to_value('{a', NULL)"
    assertEquals(null, r[0][0],
        "JT-PARSE-082: 2-arg illegal + NULL default → NULL; observed=${r}")
}
