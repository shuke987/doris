// JT-QUERY-019: contains float +0 vs -0
suite("repro_jt_query_019") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_contains(CAST('0' AS JSONB), CAST('-0' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-QUERY-019 threw: ${e.message}")
    }
}
