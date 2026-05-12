// JT-PARSE-067: jsonb_parse_error_to_null 非法 JSON → NULL
suite("repro_jt_parse_067") {
    def r = sql "SELECT jsonb_parse_error_to_null('{a:1')"
    assertEquals(null, r[0][0],
        "JT-PARSE-067: illegal JSON → NULL; observed=${r}")
}
