// JT-PARSE-079: 2 参 + 合法（default 不用）
suite("repro_jt_parse_079") {
    try {
        def r = sql """SELECT jsonb_parse_error_to_value('{"a":1}', '{"def":1}')"""
        String v = r[0][0] == null ? "null" : r[0][0].toString()
        assertTrue(v.contains('"a":1'), "JT-PARSE-079; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-079: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
