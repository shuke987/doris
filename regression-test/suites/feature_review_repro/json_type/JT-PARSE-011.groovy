// JT-PARSE-011: 嵌套 6 层 object 应通过
suite("repro_jt_parse_011") {
    def r = sql "SELECT jsonb_parse('{\"a\":{\"b\":{\"c\":{\"d\":{\"e\":{\"f\":1}}}}}}')"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"f\":1"), "JT-PARSE-011: nested object should parse; observed=${r}")
}
