// JT-COMPAT-051: ->> arrow operator
suite("repro_jt_compat_051") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT CAST('{\"a\":\"hi\"}' AS JSONB) ->> '\$.a'" }
    catch (Exception e) { threw = true }
    if (!threw) {
        assertEquals("hi", r[0][0]?.toString() ?: "", "JT-COMPAT-051; observed=${r}")
    }
}
