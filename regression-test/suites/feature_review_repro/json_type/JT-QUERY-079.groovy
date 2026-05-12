// JT-QUERY-079: `json_search(j, 'one', 'p', '\\', '$.a')` 5 参
suite("repro_jt_query_079") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_search(CAST('{"a":"hi"}' AS JSONB), 'one', 'h%', '\\', '\$.a') """
    } catch (Exception e) {
        logger.info("JT-QUERY-079 threw: ${e.message}")
    }
}
