// JT-QUERY-047: search LIKE pattern `%abc%`
suite("repro_jt_query_047") {
    def r = sql """SELECT json_search(CAST('["abc"]' AS JSONB), 'one', '%abc%')"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('$'), "observed=${r}")
}
