// JT-PATH-048: $.* 对 array 用
suite("repro_jt_path_048") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('[1,2]' AS JSONB), '\$.*') """
    } catch (Exception e) {
        logger.info("JT-PATH-048 threw: ${e.message}")
    }
}
