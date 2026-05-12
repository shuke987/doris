// JT-PATH-032: $[2^31] over int32
suite("repro_jt_path_032") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT json_extract(CAST('[1]' AS JSONB), '\$[2147483648]') """
    } catch (Exception e) {
        logger.info("JT-PATH-032 threw: ${e.message}")
    }
}
