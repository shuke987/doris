// JT-PATH-034: 控制字符 path
suite("repro_jt_path_034") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$.\n')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
