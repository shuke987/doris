// JT-EXTRACT-052: extract_int 对 bool true — spec: 不当 1 → NULL
suite("repro_jt_extract_052") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":true}' AS JSONB), '\$.a')"
        assertEquals(null, r[0][0],
            "JT-EXTRACT-052 (SEV): extract_int on bool should return NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-052: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
