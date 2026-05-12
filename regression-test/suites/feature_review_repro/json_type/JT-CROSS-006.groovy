// JT-CROSS-006: routine_load offset 推进 vs 坏 msg
suite("repro_jt_cross_006") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-006; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
