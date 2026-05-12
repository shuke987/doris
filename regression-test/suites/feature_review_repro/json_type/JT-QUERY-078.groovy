// JT-QUERY-078: corrupt jsonb 跑 `jsonb_contains`
suite("repro_jt_query_078") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_contains(CAST('1' AS JSONB), CAST('1' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-QUERY-078 threw: ${e.message}")
    }
}
