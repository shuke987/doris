// JT-PARSE-113: 科学计数
suite("repro_jt_parse_113") {
    def r = sql "SELECT jsonb_type(jsonb_parse('1e10'), '\$')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "double" || t.contains("double"),
        "JT-PARSE-113; observed=${r}")
}
