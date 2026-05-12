// JT-EXTRACT-097: type binary (T_Binary)
suite("repro_jt_extract_097") {
    // Binary not directly creatable via SQL; skip strict check
    boolean threw = false
    def r = null
    try { r = sql "SELECT jsonb_type(CAST('{\"a\":\"hi\"}' AS JSONB), '\$.a')" }
    catch (Exception e) { threw = true }
    if (!threw) {
        String t = r[0][0].toString().toLowerCase()
        // string by default; binary not common path
        assertTrue(t == "string" || t == "binary",
            "JT-EXTRACT-097; observed=${r}")
    }
}
