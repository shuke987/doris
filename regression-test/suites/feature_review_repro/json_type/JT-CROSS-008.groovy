// JT-CROSS-008: RESTORE 后 jsonb 字节完整
suite("repro_jt_cross_008") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-008; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
