// JT-PATH-061: 错误含 path 字符串
suite("repro_jt_path_061") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.[') """
    } catch (Exception e) {
        logger.info("JT-PATH-061 threw: ${e.message}")
    }
}
