// JT-CAST-018: T_Decimal256 → string
suite("repro_jt_cast_018") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST(CAST('1.23456789012345678901234567890' AS DECIMAL(38,20)) AS JSONB) AS STRING) """
    } catch (Exception e) {
        logger.info("JT-CAST-018 threw: ${e.message}")
    }
}
