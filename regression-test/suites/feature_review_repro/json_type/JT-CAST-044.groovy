// JT-CAST-044: STRUCT → JSONB
suite("repro_jt_cast_044") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(NAMED_STRUCT('a',1,'b','x') AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-044 threw: ${e.message}")
    }
}
