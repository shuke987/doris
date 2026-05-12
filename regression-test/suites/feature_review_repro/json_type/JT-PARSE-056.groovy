// JT-PARSE-056: float vs double 自动选
suite("repro_jt_parse_056") {
    def r = sql "SELECT jsonb_type(jsonb_parse('1.5'), '\$')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "double" || t == "float" || t.contains("double"),
        "JT-PARSE-056: 1.5 should be double; observed=${r}")
}
