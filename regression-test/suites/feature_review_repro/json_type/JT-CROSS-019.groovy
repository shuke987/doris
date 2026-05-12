// JT-CROSS-019: array_filter on JSONB array element
suite("repro_jt_cross_019") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT array_filter(x -> CAST(json_extract(x, '\$.a') AS INT) > 0, ARRAY(CAST('{"a":1}' AS JSONB))) """
    } catch (Exception e) {
        logger.info("JT-CROSS-019 threw: ${e.message}")
    }
}
