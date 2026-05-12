// JT-QUERY-049: search 中文 pattern
suite("repro_jt_query_049") {
    def r = sql """SELECT json_search(CAST('["中文"]' AS JSONB), 'one', '中文')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('$'), "observed=${r}")
}
