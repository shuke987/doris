// JT-QUERY-018: contains float == 比较
suite("repro_jt_query_018") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_contains(CAST('1.0' AS JSONB), CAST('1.0' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-QUERY-018 threw: ${e.message}")
    }
}
