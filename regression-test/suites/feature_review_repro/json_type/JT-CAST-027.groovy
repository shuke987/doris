// JT-CAST-027: jsonb T_Array → INT
suite("repro_jt_cast_027") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('[1,2,3]' AS JSONB) AS INT) """
    } catch (Exception e) {
        logger.info("JT-CAST-027 threw: ${e.message}")
    }
}
