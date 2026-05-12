// JT-BASE-058: `strip_null_value(j)` 非 top-level T_Null
suite("repro_jt_base_058") {
    try {
        def r = sql """SELECT json_strip_nulls(CAST('{"a":null}' AS JSONB))"""
        String v = r[0][0] == null ? "null" : r[0][0].toString()
        assertTrue(v.contains('{'), "observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-BASE-058: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
