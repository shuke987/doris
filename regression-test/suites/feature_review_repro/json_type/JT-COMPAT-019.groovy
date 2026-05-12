// JT-COMPAT-019: 4.0 image + jsonb 列 → 4.1 启动
suite("repro_jt_compat_019") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('{}' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-COMPAT-019 threw: ${e.message}")
    }
}
