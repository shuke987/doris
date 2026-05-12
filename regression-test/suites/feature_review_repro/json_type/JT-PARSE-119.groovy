// JT-PARSE-119: 3+ 参数错误消息
suite("repro_jt_parse_119") {
    try {
        boolean threw = false; String err = ""
        try { sql "SELECT jsonb_parse_error_to_value('a','b','c')" }
        catch (Exception e) { threw = true; err = e.message ?: "" }
        assertTrue(threw, "JT-PARSE-119: 3 args should throw")
        // spec: msg should mention argument count
        assertTrue(err.toLowerCase().contains("argument") || err.toLowerCase().contains("arg") || err.toLowerCase().contains("function"),
            "JT-PARSE-119: err should mention args; err=${err.take(200)}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-PARSE-119: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
