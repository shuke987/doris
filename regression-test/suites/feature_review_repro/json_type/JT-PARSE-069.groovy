// JT-PARSE-069: jsonb_parse_error_to_null 空字符串 → NULL
suite("repro_jt_parse_069") {
    def r = sql "SELECT jsonb_parse_error_to_null('')"
    assertEquals(null, r[0][0],
        "JT-PARSE-069: empty str → NULL; observed=${r}")
}
