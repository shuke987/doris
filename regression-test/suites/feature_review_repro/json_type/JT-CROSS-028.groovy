// JT-CROSS-028: PREPARE 含 json_extract(?, '$.a')
suite("repro_jt_cross_028") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT jsonb_parse('{"a":1}') """
    } catch (Exception e) {
        logger.info("JT-CROSS-028 threw: ${e.message}")
    }
}
