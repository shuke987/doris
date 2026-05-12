// JT-CAST-056: DATE → JSONB 应拒绝（spec §3.5）
suite("repro_jt_cast_056") {
    boolean threw = false
    try { sql "SELECT CAST(CAST('2024-01-01' AS DATE) AS JSONB)" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CAST-056: DATE → JSONB rejected (consistent with DATETIME)")
}
