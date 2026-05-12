// JT-CAST-059: BITMAP → JSONB 应拒绝
suite("repro_jt_cast_059") {
    boolean threw = false
    try { sql "SELECT CAST(bitmap_empty() AS JSONB)" }
    catch (Exception e) { threw = true }
    assertTrue(threw, "JT-CAST-059: BITMAP→JSONB should fail")
}
