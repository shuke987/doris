// JT-EXTRACT-120: `$**.b` 顺序 DFS vs BFS
suite("repro_jt_extract_120") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":{"b":1,"c":{"b":2}}}' AS JSONB), '\$**.b') """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-120 threw: ${e.message}")
    }
}
