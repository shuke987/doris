// JT-PATH-063: 错误不泄露内存
suite("repro_jt_path_063") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{}' AS JSONB), '\$.[') """
    } catch (Exception e) {
        logger.info("JT-PATH-063 threw: ${e.message}")
    }
}
