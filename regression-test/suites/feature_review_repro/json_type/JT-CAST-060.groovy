// JT-CAST-060: HLL → JSONB 应拒绝
suite("repro_jt_cast_060") {
    boolean threw = false
    try { sql "SELECT CAST(hll_empty() AS JSONB)" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CAST-060: HLL→JSONB should fail")
}
