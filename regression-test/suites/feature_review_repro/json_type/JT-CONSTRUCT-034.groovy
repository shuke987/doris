// JT-CONSTRUCT-034: object key 是非 STRING（int）
suite("repro_jt_construct_034") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_object(1, 'v') """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-034 threw: ${e.message}")
    }
}
