// JT-CROSS-021: JSONB × subquery
suite("repro_jt_cross_021") {
    try {
        def r = sql """
            SELECT jsonb_extract_int(j, '\$.a') FROM (SELECT CAST('{\"a\":42}' AS JSONB) AS j) t
        """
        assertEquals("42", r[0][0].toString(), "JT-CROSS-021; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CROSS-021: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
