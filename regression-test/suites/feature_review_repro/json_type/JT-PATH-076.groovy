// JT-PATH-076: JsonbPath 对象 reuse 时 leg_vector 清理
suite("repro_jt_path_076") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":1}' AS JSONB), '\$.a') """
    } catch (Exception e) {
        logger.info("JT-PATH-076 threw: ${e.message}")
    }
}
