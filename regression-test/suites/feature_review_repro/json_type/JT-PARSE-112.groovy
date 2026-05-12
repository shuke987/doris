// JT-PARSE-112: 含小数点
suite("repro_jt_parse_112") {
    def r = sql "SELECT jsonb_type(jsonb_parse('1.0'), '\$')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "double" || t.contains("double") || t == "decimal",
        "JT-PARSE-112; observed=${r}")
}
