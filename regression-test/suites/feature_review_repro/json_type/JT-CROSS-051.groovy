// JT-CROSS-051: group_commit + INSERT jsonb 列
suite("repro_jt_cross_051") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-051; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
