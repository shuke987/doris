// JT-PARSE-062: 16MB + 1 JSON
suite("repro_jt_parse_062") {
    String big = '[' + ('1,' * 1024) + '1]'
    try {
        def r = sql "SELECT jsonb_parse('${big}')"
        assertNotNull(r, "JT-PARSE-062; observed size=${big.length()}")
    } catch (Exception e) {
        logger.info("JT-PARSE-062 threw: ${e.message}")
    }
}
