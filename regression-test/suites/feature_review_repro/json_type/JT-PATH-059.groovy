// JT-PATH-059: json_remove + $**
suite("repro_jt_path_059") {
    boolean threw = false
    try { sql "SELECT json_remove(CAST('{}' AS JSONB), '\$**')" } catch (Exception e) { threw=true }
    assertTrue(threw, "JT-PATH-059: wildcard path on json_remove should reject")
}
