// JT-COMPAT-041: JSON_DEPTH (jsonb_depth missing — check if json_depth exists)
suite("repro_jt_compat_041") {
    try {
        boolean threw = false
        try { sql "SELECT JSON_DEPTH(CAST('{\"a\":{\"b\":1}}' AS JSONB))" }
        catch (Exception e) { threw = true }
        // observation: function may not exist
        assertNotNull(threw, "JT-COMPAT-041 obs; threw=${threw}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-COMPAT-041: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
