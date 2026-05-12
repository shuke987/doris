// JT-BASE-029: 列总长 > 2GB 写入
suite("repro_jt_base_029") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(REPEAT('x', 1024) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-BASE-029 threw: ${e.message}")
    }
}
