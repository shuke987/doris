// JT-PATH-055: json_set + $.*
suite("repro_jt_path_055") {
    boolean threw = false
    try { sql "SELECT json_set(CAST('{}' AS JSONB), '\$.*', 1)" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-PATH-055: wildcard path on json_set should reject")
}
