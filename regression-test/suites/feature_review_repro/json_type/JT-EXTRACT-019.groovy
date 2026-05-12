// JT-EXTRACT-019: 空 path 应拒绝
suite("repro_jt_extract_019") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-019: empty path should be rejected")
}
