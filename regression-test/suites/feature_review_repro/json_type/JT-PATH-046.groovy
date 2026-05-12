// JT-PATH-046: $.* 空 object
suite("repro_jt_path_046") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.*') """
    } catch (Exception e) {
        logger.info("JT-PATH-046 threw: ${e.message}")
    }
}
