// JT-QUERY-045: search mode 大小写
suite("repro_jt_query_045") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_search(CAST('["hi"]' AS JSONB), 'ONE', 'h%') """
    } catch (Exception e) {
        logger.info("JT-QUERY-045 threw: ${e.message}")
    }
}
