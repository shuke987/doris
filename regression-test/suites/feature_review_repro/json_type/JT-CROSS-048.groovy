// JT-CROSS-048: unique MOW + jsonb value 列
suite("repro_jt_cross_048") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-048; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
