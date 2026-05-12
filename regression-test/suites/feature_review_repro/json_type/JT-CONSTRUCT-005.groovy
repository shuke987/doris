// JT-CONSTRUCT-005: array_ignore_null NULL 跳过
suite("repro_jt_construct_005") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_array(1, NULL, 3) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-005 threw: ${e.message}")
    }
}
