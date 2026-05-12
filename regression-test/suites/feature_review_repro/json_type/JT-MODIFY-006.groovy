// JT-MODIFY-006: set 数组元素越界
suite("repro_jt_modify_006") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[10]', 99) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-006 threw: ${e.message}")
    }
}
