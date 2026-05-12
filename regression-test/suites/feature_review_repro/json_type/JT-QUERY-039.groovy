// JT-QUERY-039: search one 字符串
suite("repro_jt_query_039") {
    def r = sql """SELECT json_search(CAST('["hi","hello"]' AS JSONB), 'one', 'h%')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('$'), "observed=${r}")
}
