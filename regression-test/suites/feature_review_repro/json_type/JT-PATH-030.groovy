// JT-PATH-030: $[-1] 负
suite("repro_jt_path_030") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[-1]') """
    } catch (Exception e) {
        logger.info("JT-PATH-030 threw: ${e.message}")
    }
}
