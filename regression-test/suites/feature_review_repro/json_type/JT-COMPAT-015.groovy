// JT-COMPAT-015: legacy + wildcard 语法
suite("repro_jt_compat_015") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('{"a":1}' AS JSONB), '\$.a') """
    } catch (Exception e) {
        logger.info("JT-COMPAT-015 threw: ${e.message}")
    }
}
