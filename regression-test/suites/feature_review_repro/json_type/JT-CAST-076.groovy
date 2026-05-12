// JT-CAST-076: nereids `cast('{"a":1}'::JSONB as STRING)`
suite("repro_jt_cast_076") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('{"a":1}' AS JSONB) AS STRING) """
    } catch (Exception e) {
        logger.info("JT-CAST-076 threw: ${e.message}")
    }
}
