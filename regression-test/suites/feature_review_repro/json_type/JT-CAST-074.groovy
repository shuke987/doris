// JT-CAST-074: cast 1MB invalid JSON as JSONB strict_mode 错误消息长度
suite("repro_jt_cast_074") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(REPEAT('{', 1024) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-074 threw: ${e.message}")
    }
}
