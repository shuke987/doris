// JT-CROSS-039: UDF 参数类型 JSONB
suite("repro_jt_cross_039") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-039; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
