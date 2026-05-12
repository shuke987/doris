// JT-CMP-001: JSONB = JSONB 应拒绝
suite("repro_jt_cmp_001") {
    boolean threw = false
    try { sql "SELECT CAST('1' AS JSONB) = CAST('1' AS JSONB)" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CMP-001: equality on JSONB should be rejected")
}
