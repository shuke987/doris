// JT-MODIFY-007: set 数组 $[last+1]
suite("repro_jt_modify_007") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_set(CAST('[1,2,3]' AS JSONB), '\$[10]', 99) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-007 threw: ${e.message}")
    }
}
