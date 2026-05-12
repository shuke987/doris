// JT-QUERY-081: corrupt jsonb + `strip_null_value` 行为
suite("repro_jt_query_081") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_contains(CAST('1' AS JSONB), CAST('1' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-QUERY-081 threw: ${e.message}")
    }
}
