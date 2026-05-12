// JT-EXTRACT-084: exists_path 非法 path 应拒绝
suite("repro_jt_extract_084") {
    boolean threw = false
    try { sql "SELECT jsonb_exists_path(CAST('{\"a\":1}' AS JSONB), '.a')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-084: illegal path should reject")
}
