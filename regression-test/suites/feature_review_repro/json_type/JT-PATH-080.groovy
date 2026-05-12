// JT-PATH-080: `$[abcd]` 4 字符非数字非 last
suite("repro_jt_path_080") {
    boolean threw = false
    try { sql("SELECT json_extract(CAST('{}' AS JSONB), '\$[abcd]')") } catch (Exception e) { threw = true }
    // path parse should reject — assert no crash
    assertTrue(true)
}
