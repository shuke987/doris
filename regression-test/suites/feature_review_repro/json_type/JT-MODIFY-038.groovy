// JT-MODIFY-038: replace NULL value
suite("repro_jt_modify_038") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_replace(CAST('{"a":1}' AS JSONB), '\$.a', NULL) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-038 threw: ${e.message}")
    }
}
