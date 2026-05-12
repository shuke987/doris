// JT-CMP-002: JSONB < JSONB 应拒绝
suite("repro_jt_cmp_002") {
    boolean threw = false
    try { sql "SELECT CAST('1' AS JSONB) < CAST('2' AS JSONB)" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CMP-002: less-than on JSONB should be rejected")
}
