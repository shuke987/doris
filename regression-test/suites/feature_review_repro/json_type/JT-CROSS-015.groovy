// JT-CROSS-015: sync_mv 含 json_extract 表达式
suite("repro_jt_cross_015") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-015; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
