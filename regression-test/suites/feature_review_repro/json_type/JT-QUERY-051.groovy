// JT-QUERY-051: json_valid 非法
suite("repro_jt_query_051") {
    def r = sql "SELECT json_valid('{a:1')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "0" || v == "false", "JT-QUERY-051; observed=${r}")
}
