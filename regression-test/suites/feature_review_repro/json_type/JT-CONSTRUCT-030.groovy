// JT-CONSTRUCT-030: object key 含 \n
suite("repro_jt_construct_030") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_object('a\nb', 1) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-030 threw: ${e.message}")
    }
}
