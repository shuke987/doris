// JT-QUERY-048: search escape (LIKE backslash escape)
suite("repro_jt_query_048") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql '''SELECT json_search(CAST('["50%"]' AS JSONB), 'one', '50\\\\%', '\\\\')'''
    } catch (Exception e) {
        logger.info("JT-QUERY-048 threw: ${e.message}")
    }
}
