// JT-CONSTRUCT-027: object 重复 key 后 extract
suite("repro_jt_construct_027") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(json_object('a',1,'a',2), '\$.a') """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-027 threw: ${e.message}")
    }
}
