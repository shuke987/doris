// JT-PATH-057: json_replace + $.*
suite("repro_jt_path_057") {
    boolean threw = false
    try { sql "SELECT json_replace(CAST('{}' AS JSONB), '\$.*', 1)" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-PATH-057: wildcard path on json_replace should reject")
}
