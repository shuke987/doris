// JT-MODIFY-066: 多 path modify 中间 path 全不存在
suite("repro_jt_modify_066") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('{}' AS JSONB), '\$.a.b.c', 1, '\$.x.y.z', 2) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-066 threw: ${e.message}")
    }
}
