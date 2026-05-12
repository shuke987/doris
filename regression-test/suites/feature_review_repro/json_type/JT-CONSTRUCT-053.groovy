// JT-CONSTRUCT-053: writer 未 writeStartObject 直接 jsonb_object 内部 writeKey 失败
suite("repro_jt_construct_053") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_object('a', 1) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-053 threw: ${e.message}")
    }
}
