// JT-PARSE-001: jsonb_parse 标准 object
suite("repro_jt_parse_001") {
    def r = sql "SELECT jsonb_parse('{\"a\":1}')"
    assertEquals("{\"a\":1}", r[0][0].toString(),
        "JT-PARSE-001: standard object; observed=${r}")
}
