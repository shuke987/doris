// JT-PARSE-084: 3 参 应拒绝
suite("repro_jt_parse_084") {
    try {
        boolean threw = false
        try { sql "SELECT jsonb_parse_error_to_value('a','b','c')" }
        catch (Exception e) { threw = true }
        assertTrue(threw, "JT-PARSE-084: 3-arg should throw")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-084: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
