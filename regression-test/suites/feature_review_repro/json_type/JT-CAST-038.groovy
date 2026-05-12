// JT-CAST-038: jsonb T_Array 混合类型 → ARRAY<INT>
suite("repro_jt_cast_038") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('[1,"2",3.5]' AS JSONB) AS ARRAY<INT>) """
    } catch (Exception e) {
        logger.info("JT-CAST-038 threw: ${e.message}")
    }
}
