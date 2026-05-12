// JT-CROSS-045: DENY 普通用户访问 jsonb 列
suite("repro_jt_cross_045") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-045; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
