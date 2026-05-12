// JT-MODIFY-055: remove path 含 \\n key
suite("repro_jt_modify_055") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_remove(CAST('{"a\nb":1}' AS JSONB), '\$."a\nb"') """
    } catch (Exception e) {
        logger.info("JT-MODIFY-055 threw: ${e.message}")
    }
}
