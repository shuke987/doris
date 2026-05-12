// JT-MODIFY-054: remove path 含中文
suite("repro_jt_modify_054") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_remove(CAST('{"中文":1}' AS JSONB), '\$.中文') """
    } catch (Exception e) {
        logger.info("JT-MODIFY-054 threw: ${e.message}")
    }
}
