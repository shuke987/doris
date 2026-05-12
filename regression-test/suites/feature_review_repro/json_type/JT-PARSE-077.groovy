// JT-PARSE-077: jsonb_parse_error_to_value 1 参 合法
suite("repro_jt_parse_077") {
    def r = sql "SELECT jsonb_parse_error_to_value('{\"a\":1}')"
    assertEquals("{\"a\":1}", r[0][0].toString(),
        "JT-PARSE-077: 1-arg legal; observed=${r}")
}
