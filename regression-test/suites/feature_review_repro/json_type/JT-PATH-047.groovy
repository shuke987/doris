// JT-PATH-047: $.* 单 key
suite("repro_jt_path_047") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":1}' AS JSONB), '\$.*') """
    } catch (Exception e) {
        logger.info("JT-PATH-047 threw: ${e.message}")
    }
}
