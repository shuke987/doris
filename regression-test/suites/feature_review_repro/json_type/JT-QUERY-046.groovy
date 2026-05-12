// JT-QUERY-046: search mode 非法
suite("repro_jt_query_046") {
    boolean threw = false
    try { sql "SELECT json_search(CAST('[\"hi\"]' AS JSONB), 'foo', 'h%')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-QUERY-046: should reject; observed=no exception")
}
