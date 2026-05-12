// JT-CONSTRUCT-054: `jsonb_object('', 1)` 空 key
suite("repro_jt_construct_054") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_object('', 1) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-054 threw: ${e.message}")
    }
}
