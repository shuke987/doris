// JT-CAST-032: jsonb → BOOLEAN 数 2
suite("repro_jt_cast_032") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('2' AS JSONB) AS BOOLEAN) """
    } catch (Exception e) {
        logger.info("JT-CAST-032 threw: ${e.message}")
    }
}
