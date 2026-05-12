// JT-MODIFY-016: set wildcard path
suite("repro_jt_modify_016") {
    boolean threw = false
    try { sql "SELECT json_set(CAST('{}' AS JSONB), '\$.*', 1)" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-MODIFY-016: should reject; observed=no exception")
}
