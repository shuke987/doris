// JT-CAST-004: CAST 'invalid' AS JSONB + strict_mode=true 应报错
suite("repro_jt_cast_004") {
    sql "SET enable_strict_cast=true"
    boolean threw = false
    try { sql "SELECT CAST('invalid' AS JSONB)" }
    catch (Exception e) { threw = true }
    sql "SET enable_strict_cast=default"
    assertTrue(threw, "JT-CAST-004: strict_cast on invalid → should throw")
}
