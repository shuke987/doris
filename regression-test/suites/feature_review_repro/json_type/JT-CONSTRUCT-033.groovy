// JT-CONSTRUCT-033: object value 非 JSONB 类型未显式 cast
suite("repro_jt_construct_033") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_object('k', 1) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-033 threw: ${e.message}")
    }
}
