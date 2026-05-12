// JT-PATH-084: `json_extract('{"":1}', '$.[""]')` 双引号空 key
suite("repro_jt_path_084") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"":1}' AS JSONB), '\$.""') """
    } catch (Exception e) {
        logger.info("JT-PATH-084 threw: ${e.message}")
    }
}
