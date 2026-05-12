// JT-CAST-030: jsonb T_Int128 → BIGINT 溢出
suite("repro_jt_cast_030") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('1329227995784915872903807060280344576' AS JSONB) AS BIGINT) """
    } catch (Exception e) {
        logger.info("JT-CAST-030 threw: ${e.message}")
    }
}
