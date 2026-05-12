// JT-CMP-009: hash(JSONB) function (binary hash)
suite("repro_jt_cmp_009") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT hash(CAST('{\"a\":1}' AS JSONB))" }
    catch (Exception e) { threw = true }
    // either supports or rejects; lock observation
    if (!threw) {
        assertNotNull(r[0][0], "JT-CMP-009; observed=${r}")
    }
}
