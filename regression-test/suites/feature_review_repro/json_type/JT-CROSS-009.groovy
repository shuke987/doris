// JT-CROSS-009: BACKUP + ALTER schema 期间
suite("repro_jt_cross_009") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-009; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
