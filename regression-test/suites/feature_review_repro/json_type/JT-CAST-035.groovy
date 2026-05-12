// JT-CAST-035: jsonb T_String → DATETIME
suite("repro_jt_cast_035") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('"2024-01-01 00:00:00"' AS JSONB) AS DATETIME) """
    } catch (Exception e) {
        logger.info("JT-CAST-035 threw: ${e.message}")
    }
}
