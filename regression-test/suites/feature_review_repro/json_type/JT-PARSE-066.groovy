// JT-PARSE-066: jsonb_parse_error_to_null 合法 JSON
suite("repro_jt_parse_066") {
    def r = sql "SELECT jsonb_parse_error_to_null('{\"a\":1}')"
    assertEquals("{\"a\":1}", r[0][0].toString(),
        "JT-PARSE-066: legal JSON; observed=${r}")
}
