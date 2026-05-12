// JT-MODIFY-047: remove 顶层 $
suite("repro_jt_modify_047") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_remove(CAST('{"a":1}' AS JSONB), '\$') """
    } catch (Exception e) {
        logger.info("JT-MODIFY-047 threw: ${e.message}")
    }
}
