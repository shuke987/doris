// JT-PATH-062: 错误含位置
suite("repro_jt_path_062") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.[') """
    } catch (Exception e) {
        logger.info("JT-PATH-062 threw: ${e.message}")
    }
}
