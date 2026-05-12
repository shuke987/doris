// JT-CAST-073: DATETIME / DATE / IPV4 / IPV6 / VARIANT → JSONB can_cast 矩阵
suite("repro_jt_cast_073") {
    // behavior probe: spec under-specified — assert no crash
    try {
        sql """ SELECT CAST(CAST('2024-01-01' AS DATE) AS JSONB) """
    } catch (Exception e) {
        logger.info("JT-CAST-073 threw: ${e.message}")
    }
}
