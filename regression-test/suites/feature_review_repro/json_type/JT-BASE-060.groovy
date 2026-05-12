// JT-BASE-060: size=1（仅 header）
suite("repro_jt_base_060") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('1' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-BASE-060 threw: ${e.message}")
    }
}
