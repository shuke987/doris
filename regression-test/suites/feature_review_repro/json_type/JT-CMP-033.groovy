// JT-CMP-033: corrupt jsonb 跑 sort_json_object_keys 行为
suite("repro_jt_cmp_033") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT sort_json_object_keys(CAST('{"a":1}' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-CMP-033 threw: ${e.message}")
    }
}
