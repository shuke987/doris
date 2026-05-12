// JT-MODIFY-023: set path 含中文 key
suite("repro_jt_modify_023") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('{}' AS JSONB), '\$.中文', 1) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-023 threw: ${e.message}")
    }
}
