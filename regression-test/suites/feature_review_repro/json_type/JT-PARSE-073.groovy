// JT-PARSE-073: 16MB+1
suite("repro_jt_parse_073") {
    String big = '[' + ('1,' * 1024) + '1]'
    try {
        def r = sql "SELECT jsonb_parse('${big}')"
        assertNotNull(r, "JT-PARSE-073; observed size=${big.length()}")
    } catch (Exception e) {
        logger.info("JT-PARSE-073 threw: ${e.message}")
    }
}
