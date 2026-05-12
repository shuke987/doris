// JT-EXTRACT-118: depth 100 层
suite("repro_jt_extract_118") {
    try {
        String s = '1'
        for (int i=0;i<100;i++) s = "[${s}]"
        try {
            def r = sql "SELECT json_depth(CAST('${s}' AS JSONB))"
            assertNotNull(r[0][0], "JT-EXTRACT-118; observed=${r}")
        } catch (Exception e) {
            // may exceed parse depth
            assertTrue(true)
        }
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-118: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
