// JT-PATH-021: 不以 $ 起始 应拒绝
suite("repro_jt_path_021") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '.a')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-PATH-021: path without \$ should fail")
}
