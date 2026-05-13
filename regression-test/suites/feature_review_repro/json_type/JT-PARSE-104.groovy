// JT-PARSE-104 (HARD RULE): jsonb_parse_no_strict — if exists must work, else clearly reject
suite("repro_jt_parse_104") {
    boolean threw = false
    def r = null
    String err = ""
    try { r = sql "SELECT jsonb_parse_no_strict('{a:1}')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    if (!threw) {
        assertNotNull(r[0][0], "non-strict parse must accept non-quoted key; r=${r}")
    } else {
        assertTrue(err.toLowerCase().contains("found") || err.toLowerCase().contains("function"),
            "if not implemented, FE clear error; got=${err}")
    }
}
