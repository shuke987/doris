// JT-PATH-058: json_remove + $.*
suite("repro_jt_path_058") {
    boolean threw = false
    try { sql "SELECT json_remove(CAST('{}' AS JSONB), '\$.*')" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-PATH-058: wildcard path on json_remove should reject")
}
