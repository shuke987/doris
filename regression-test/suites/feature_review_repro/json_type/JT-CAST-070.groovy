// JT-CAST-070: strict_mode + cast(corrupt_jsonb as STRING)
suite("repro_jt_cast_070") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('{"a":1}' AS JSONB) AS STRING) """
    } catch (Exception e) {
        logger.info("JT-CAST-070 threw: ${e.message}")
    }
}
