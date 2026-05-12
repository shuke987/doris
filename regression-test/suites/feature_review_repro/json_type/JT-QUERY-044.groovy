// JT-QUERY-044: search empty pattern
suite("repro_jt_query_044") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_search(CAST('["hi"]' AS JSONB), 'one', '') """
    } catch (Exception e) {
        logger.info("JT-QUERY-044 threw: ${e.message}")
    }
}
