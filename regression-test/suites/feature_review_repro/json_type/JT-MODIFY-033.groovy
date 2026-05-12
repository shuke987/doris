// JT-MODIFY-033: insert wildcard
suite("repro_jt_modify_033") {
    boolean threw = false
    try { sql "SELECT json_insert(CAST('{}' AS JSONB), '\$.*', 1)" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-MODIFY-033: should reject; observed=no exception")
}
