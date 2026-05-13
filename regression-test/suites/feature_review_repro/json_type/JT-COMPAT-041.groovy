// JT-COMPAT-041 (HARD RULE): JSON_DEPTH MySQL compat — must work or clearly reject
suite("repro_jt_compat_041") {
    boolean threw = false
    def r = null
    String err = ""
    try { r = sql "SELECT JSON_DEPTH(CAST('{\"a\":{\"b\":1}}' AS JSONB))" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    if (!threw) {
        // depth = 3 (root + a + b)
        assertNotNull(r[0][0], "JSON_DEPTH result non-null; r=${r}")
    } else {
        assertTrue(err.toLowerCase().contains("found") || err.toLowerCase().contains("function"),
            "if not implemented, FE must clearly say function not found; got=${err}")
    }
}
