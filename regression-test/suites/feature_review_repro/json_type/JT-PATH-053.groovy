// JT-PATH-053: json_keys + $.*
suite("repro_jt_path_053") {
    boolean threw = false
    try { sql "SELECT json_keys(CAST('{}' AS JSONB), '\$.*')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-053: should reject; observed=no exception")
}
