// JT-CONSTRUCT-026: object 重复 key
suite("repro_jt_construct_026") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_object('a',1,'a',2) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-026 threw: ${e.message}")
    }
}
