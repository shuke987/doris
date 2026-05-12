// JT-PARSE-080: jsonb_parse_error_to_value 2 参 非法 → default '[1]'
suite("repro_jt_parse_080") {
    def r = sql "SELECT jsonb_parse_error_to_value('{a', '[1]')"
    assertEquals("[1]", r[0][0].toString(),
        "JT-PARSE-080: 2-arg illegal → user default; observed=${r}")
}
