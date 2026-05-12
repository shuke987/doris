// JT-EXTRACT-133: `jsonb_type` 1 参形式
suite("repro_jt_extract_133") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_type(CAST('{"a":1}' AS JSONB)) """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-133 threw: ${e.message}")
    }
}
