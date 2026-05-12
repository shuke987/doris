// JT-CAST-043: ARRAY<JSONB> → JSONB
suite("repro_jt_cast_043") {
    def r = null; boolean threw = false
    try { r = sql "SELECT CAST(ARRAY(CAST('{\"a\":1}' AS JSONB), CAST('{\"b\":2}' AS JSONB)) AS JSONB)" }
    catch (Exception e) { threw = true }
    if (!threw) {
        String v = r[0][0].toString()
        assertTrue(v.startsWith("[") && v.contains("\"a\":1"),
            "JT-CAST-043; observed=${r}")
    }
}
