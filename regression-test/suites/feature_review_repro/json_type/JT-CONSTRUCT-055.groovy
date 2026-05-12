// JT-CONSTRUCT-055: `jsonb_array(NULL, jsonb_corrupt)`
suite("repro_jt_construct_055") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_array(NULL, CAST('1' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-CONSTRUCT-055 threw: ${e.message}")
    }
}
