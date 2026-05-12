// JT-MODIFY-034: insert 嵌套 path 中间不存在
suite("repro_jt_modify_034") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_insert(CAST('{}' AS JSONB), '\$.a.b', 1) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-034 threw: ${e.message}")
    }
}
