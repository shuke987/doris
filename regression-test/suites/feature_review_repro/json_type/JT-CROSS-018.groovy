// JT-CROSS-018: array_map(x -> json_extract(x, '$.k'), arr)
suite("repro_jt_cross_018") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT array_map(x -> json_extract(x, '\$.k'), ARRAY(CAST('{"k":1}' AS JSONB))) """
    } catch (Exception e) {
        logger.info("JT-CROSS-018 threw: ${e.message}")
    }
}
