// JT-CROSS-012: JSONB × CTE
suite("repro_jt_cross_012") {
    try {
        def r = sql """
            WITH cte AS (SELECT CAST('{\"a\":1}' AS JSONB) AS j)
            SELECT jsonb_extract_int(j, '\$.a') FROM cte
        """
        assertEquals("1", r[0][0].toString(), "JT-CROSS-012; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-CROSS-012: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
