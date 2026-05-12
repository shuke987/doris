// JT-PARSE-078: jsonb_parse_error_to_value 1 参 非法 → {}
suite("repro_jt_parse_078") {
    def r = sql "SELECT jsonb_parse_error_to_value('{a:1')"
    assertEquals("{}", r[0][0].toString(),
        "JT-PARSE-078: 1-arg illegal → default '{}'; observed=${r}")
}
