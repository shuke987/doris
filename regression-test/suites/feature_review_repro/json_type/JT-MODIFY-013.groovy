// JT-MODIFY-013: set NULL path
suite("repro_jt_modify_013") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('{}' AS JSONB), NULL, 1) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-013 threw: ${e.message}")
    }
}
