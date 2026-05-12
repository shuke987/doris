// JT-QUERY-050: json_valid 合法
suite("repro_jt_query_050") {
    def r = sql "SELECT json_valid('{\"a\":1}')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-QUERY-050; observed=${r}")
}
