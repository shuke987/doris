// JT-PATH-056: json_insert + $.*
suite("repro_jt_path_056") {
    boolean threw = false
    try { sql "SELECT json_insert(CAST('{}' AS JSONB), '\$.*', 1)" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-PATH-056: wildcard path on json_insert should reject")
}
