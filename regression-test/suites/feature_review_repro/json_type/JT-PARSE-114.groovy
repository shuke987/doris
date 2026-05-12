// JT-PARSE-114: 大整数 2^127-1
suite("repro_jt_parse_114") {
    def r = sql "SELECT jsonb_type(jsonb_parse('170141183460469231731687303715884105727'), '\$')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t.contains("int") || t == "double",
        "JT-PARSE-114; observed=${r}")
}
