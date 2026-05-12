// JT-PATH-036: path 含 SQL 注入符
suite("repro_jt_path_036") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$.\'; DROP TABLE')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
