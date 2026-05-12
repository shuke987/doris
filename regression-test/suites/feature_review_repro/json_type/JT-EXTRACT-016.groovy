// JT-EXTRACT-016: 多 path 1 个非法 应拒绝
suite("repro_jt_extract_016") {
    boolean threw = false
    try { sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.a', 'BAD')" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-EXTRACT-016: illegal path in multi-path should throw")
}
