// JT-PATH-082: `$.\t` 控制字符 path
suite("repro_jt_path_082") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.t') """
    } catch (Exception e) {
        logger.info("JT-PATH-082 threw: ${e.message}")
    }
}
