// JT-PARSE-110: 整数选最小类型 — array of mixed sizes
suite("repro_jt_parse_110") {
    def r = sql "SELECT jsonb_parse('[1, 200, 70000, 5000000000]')"
    String v = r[0][0].toString()
    assertTrue(v.contains("5000000000") && v.contains("70000"),
        "JT-PARSE-110; observed=${r}")
}
