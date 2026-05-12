// JT-EXTRACT-020: path 不以 $ 起始 应拒绝
suite("repro_jt_extract_020") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '.a')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-020: path not starting with \$ should be rejected")
}
