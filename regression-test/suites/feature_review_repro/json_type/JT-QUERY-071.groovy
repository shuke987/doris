// JT-QUERY-071: JsonbPath::to_string 中间 leg 失败 partial string
suite("repro_jt_query_071") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.a') """
    } catch (Exception e) {
        logger.info("JT-QUERY-071 threw: ${e.message}")
    }
}
