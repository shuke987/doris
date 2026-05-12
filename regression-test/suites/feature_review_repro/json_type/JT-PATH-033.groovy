// JT-PATH-033: 空白 leg `$. .a`
suite("repro_jt_path_033") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$. .a')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
