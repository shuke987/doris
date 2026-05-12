// JT-MODIFY-039: replace wildcard
suite("repro_jt_modify_039") {
    boolean threw = false
    try { sql "SELECT json_replace(CAST('{}' AS JSONB), '\$.*', 1)" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-MODIFY-039: should reject; observed=no exception")
}
