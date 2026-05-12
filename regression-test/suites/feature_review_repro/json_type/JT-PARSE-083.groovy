// JT-PARSE-083: 2 参 default 是 SQL NULL 列
suite("repro_jt_parse_083") {
    try {
        def r = sql """SELECT jsonb_parse_error_to_value('{a', NULL)"""
        assertEquals(null, r[0][0], "JT-PARSE-083: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-083: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
