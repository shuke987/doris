// JT-COMPAT-050: -> arrow operator (MySQL JSON shortcut)
suite("repro_jt_compat_050") {
    boolean threw = false
    def r = null
    try { r = sql "SELECT CAST('{\"a\":1}' AS JSONB) -> '\$.a'" }
    catch (Exception e) { threw = true }
    // observation: may or may not be supported
    if (!threw) {
        assertEquals("1", r[0][0]?.toString() ?: "", "JT-COMPAT-050; observed=${r}")
    }
}
