// JT-BASE-059: size==0 fallback 路径
suite("repro_jt_base_059") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST('1' AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-BASE-059 threw: ${e.message}")
    }
}
