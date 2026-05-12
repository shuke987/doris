// JT-BASE-057: `strip_null_value(j)` top-level T_Null
suite("repro_jt_base_057") {
    try {
        def r = sql """SELECT json_strip_nulls(CAST('null' AS JSONB))"""
        assertEquals(null, r[0][0], "JT-BASE-057: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-BASE-057: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
