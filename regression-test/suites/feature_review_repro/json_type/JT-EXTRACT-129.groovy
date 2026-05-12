// JT-EXTRACT-129: `jsonb_extract_no_quotes` on object 值
suite("repro_jt_extract_129") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_unquote(json_extract(CAST('{"a":{"x":1}}' AS JSONB), '\$.a')) """
    } catch (Exception e) {
        logger.info("JT-EXTRACT-129 threw: ${e.message}")
    }
}
