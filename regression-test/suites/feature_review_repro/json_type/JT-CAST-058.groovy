// JT-CAST-058 (HARD RULE): IPV4 → JSONB MUST succeed (string-like)
suite("repro_jt_cast_058") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT CAST(CAST('192.168.1.1' AS IPV4) AS JSONB)" }
    catch (Exception e) { threw = true }
    assertEquals(false, threw, "IPV4 → JSONB CAST MUST succeed; threw=${threw}")
    assertNotNull(r[0][0], "result must be non-null")
}
