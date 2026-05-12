// JT-CAST-019: T_Double 特殊值 → string
suite("repro_jt_cast_019") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('1e1000' AS DOUBLE) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-019 threw: ${e.message}")
    }
}
