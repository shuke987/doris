// JT-PARSE-088: default 列含 NULL + 输入非法
suite("repro_jt_parse_088") {
    try {
        def r = sql """SELECT jsonb_parse_error_to_null('bad')"""
        assertEquals(null, r[0][0], "JT-PARSE-088: expect NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-088: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
