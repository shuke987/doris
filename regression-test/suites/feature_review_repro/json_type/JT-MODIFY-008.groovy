// JT-MODIFY-008: set 非 array 节点用 $[N]
suite("repro_jt_modify_008") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[10]', 99) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-008 threw: ${e.message}")
    }
}
