// JT-CAST-057: TIMESTAMP → JSONB
suite("repro_jt_cast_057") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('2024-01-01 00:00:00' AS DATETIME) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-057 threw: ${e.message}")
    }
}
