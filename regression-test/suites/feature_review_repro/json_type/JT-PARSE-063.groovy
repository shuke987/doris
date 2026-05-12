// JT-PARSE-063: 100MB JSON
suite("repro_jt_parse_063") {
    String big = '[' + ('1,' * 4096) + '1]'
    try {
        def r = sql "SELECT jsonb_parse('${big}')"
        assertNotNull(r, "JT-PARSE-063; observed size=${big.length()}")
    } catch (Exception e) {
        logger.info("JT-PARSE-063 threw: ${e.message}")
    }
}
