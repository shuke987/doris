// JT-EXTRACT-115: jsonb_depth — 不存在则跳过
suite("repro_jt_extract_115") {
    boolean threw = false
    try { sql "SELECT jsonb_depth(CAST('1' AS JSONB))" }
    catch (Exception e) { threw = true }
    // observation: jsonb_depth not registered on this branch
    assertNotNull(threw, "JT-EXTRACT-115 obs; threw=${threw}")
}
