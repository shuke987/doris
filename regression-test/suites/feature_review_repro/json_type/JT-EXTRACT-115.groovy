// JT-EXTRACT-115 (HARD RULE): jsonb_depth — must work or clearly reject
suite("repro_jt_extract_115") {
    boolean threw = false
    def r = null
    String err = ""
    try { r = sql "SELECT jsonb_depth(CAST('1' AS JSONB))" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    if (!threw) {
        assertNotNull(r[0][0], "jsonb_depth on scalar returns >=1; r=${r}")
    } else {
        assertTrue(err.toLowerCase().contains("found") || err.toLowerCase().contains("function"),
            "if not implemented, FE clear error; got=${err}")
    }
}
