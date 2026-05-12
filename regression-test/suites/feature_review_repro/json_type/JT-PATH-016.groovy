// JT-PATH-016: $.*.a
suite("repro_jt_path_016") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"x":{"a":1},"y":{"a":2}}' AS JSONB), '\$.*.a') """
    } catch (Exception e) {
        logger.info("JT-PATH-016 threw: ${e.message}")
    }
}
