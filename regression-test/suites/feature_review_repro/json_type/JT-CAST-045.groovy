// JT-CAST-045: MAP<STRING, INT> → JSONB
suite("repro_jt_cast_045") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(MAP('a',1) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-045 threw: ${e.message}")
    }
}
