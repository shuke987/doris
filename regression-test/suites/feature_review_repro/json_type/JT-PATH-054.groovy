// JT-PATH-054: json_keys + $**
suite("repro_jt_path_054") {
    boolean threw = false
    try { sql "SELECT json_keys(CAST('{}' AS JSONB), '\$**')" } catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-054: should reject; observed=no exception")
}
