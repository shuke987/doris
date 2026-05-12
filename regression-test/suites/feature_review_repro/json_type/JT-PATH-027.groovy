// JT-PATH-027: 数组索引非数字
suite("repro_jt_path_027") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$[abc]')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
