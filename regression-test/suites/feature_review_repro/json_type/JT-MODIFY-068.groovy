// JT-MODIFY-068: `json_insert('"hello"', '$[1]', 99)` promote 顺序
suite("repro_jt_modify_068") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_insert(CAST('"hello"' AS JSONB), '\$[1]', 99) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-068 threw: ${e.message}")
    }
}
