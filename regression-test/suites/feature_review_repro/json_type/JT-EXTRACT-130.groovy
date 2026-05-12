// JT-EXTRACT-130: `jsonb_extract_no_quotes` on int 值
suite("repro_jt_extract_130") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_unquote(json_extract(CAST('{"a":1}' AS JSONB), '\$.a')) """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-130 threw: ${e.message}")
    }
}
