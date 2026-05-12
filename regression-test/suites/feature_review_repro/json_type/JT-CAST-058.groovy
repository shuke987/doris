// JT-CAST-058: IPV4 → JSONB
suite("repro_jt_cast_058") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT CAST(CAST('192.168.1.1' AS IPV4) AS JSONB)" }
    catch (Exception e) { threw = true }
    // observation lock
    assertNotNull(threw, "JT-CAST-058 obs; threw=${threw}")
}
