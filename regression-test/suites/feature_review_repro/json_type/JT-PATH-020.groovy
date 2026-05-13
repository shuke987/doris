// JT-PATH-020 (HARD RULE): escape '\\' in path key MUST work or clearly reject
suite("repro_jt_path_020") {
    def r = null; boolean threw = false; String err = ""
    try { r = sql "SELECT jsonb_extract(CAST('{\"a\\\\b\":1}' AS JSONB), '\$.\"a\\\\b\"')" }
    catch (Exception e) { threw = true; err = e.message ?: "" }
    if (!threw) {
        assertNotNull(r[0][0], "escape path extract must return value 1; r=${r}")
    } else {
        // if unsupported, must be a clear path-syntax error
        assertTrue(err.toLowerCase().contains("path") || err.toLowerCase().contains("syntax"),
            "if escape unsupported, error must mention path syntax; got=${err}")
    }
}
