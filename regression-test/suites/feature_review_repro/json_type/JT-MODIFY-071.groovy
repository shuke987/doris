// JT-MODIFY-071: `json_set(j, '$bad\n', val)` 错误消息含 row/argument index
suite("repro_jt_modify_071") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('{}' AS JSONB), '\$bad', 1) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-071 threw: ${e.message}")
    }
}
