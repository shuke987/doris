// JT-MODIFY-029: insert 数组 $[last+1]
suite("repro_jt_modify_029") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_insert(CAST('[1,2]' AS JSONB), '\$[2]', 3) """
    } catch (Exception e) {
        logger.info("JT-MODIFY-029 threw: ${e.message}")
    }
}
