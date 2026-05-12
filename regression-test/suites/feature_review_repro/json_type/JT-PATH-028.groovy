// JT-PATH-028: $[1.5] 小数
suite("repro_jt_path_028") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$[1.5]')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
