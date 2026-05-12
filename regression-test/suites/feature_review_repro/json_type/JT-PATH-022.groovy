// JT-PATH-022: 空 path 应拒绝
suite("repro_jt_path_022") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-022: empty path should fail")
}
