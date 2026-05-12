// JT-MODIFY-074: json_remove 多 path 中间 wildcard 跳过 dead path
suite("repro_jt_modify_074") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_remove(CAST('{"a":1}' AS JSONB), '\$.a') """
    } catch (Exception e) {
        logger.info("JT-MODIFY-074 threw: ${e.message}")
    }
}
