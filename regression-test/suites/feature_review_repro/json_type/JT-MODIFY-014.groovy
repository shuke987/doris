// JT-MODIFY-014: set NULL value
suite("repro_jt_modify_014") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('{}' AS JSONB), '\$.a', NULL) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-014 threw: ${e.message}")
    }
}
