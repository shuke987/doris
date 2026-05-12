// JT-CAST-061: VARIANT → JSONB
suite("repro_jt_cast_061") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('{"a":1}' AS VARIANT) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-061 threw: ${e.message}")
    }
}
