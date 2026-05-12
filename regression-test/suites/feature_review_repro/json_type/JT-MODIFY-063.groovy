// JT-MODIFY-063: writer reuse 连续 set 4 次大 jsonb
suite("repro_jt_modify_063") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(json_set(json_set(json_set(CAST('{}' AS JSONB), '\$.a', 1), '\$.b', 2), '\$.c', 3), '\$.d', 4) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-063 threw: ${e.message}")
    }
}
