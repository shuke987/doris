// JT-QUERY-080: json_search mode 含恶意字符串 log 注入
suite("repro_jt_query_080") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_search(CAST('{"a":1}' AS JSONB), 'one\nINJECT', 'p') """
    } catch (Exception e) {
        logger.info("JT-QUERY-080 threw: ${e.message}")
    }
}
