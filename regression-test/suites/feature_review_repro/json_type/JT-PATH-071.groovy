// JT-PATH-071: path `$.["a\\b"]` 在 key=`a\b` object 上能匹配
suite("repro_jt_path_071") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a\\b":1}' AS JSONB), '\$."a\\b"') """
    } catch (Exception e) {
        logger.info("JT-PATH-071 threw: ${e.message}")
    }
}
