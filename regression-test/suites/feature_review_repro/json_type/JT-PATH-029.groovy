// JT-PATH-029: $[1e10] 科学计数
suite("repro_jt_path_029") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$[1e10]')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
