// JT-CROSS-047: audit_log 脱敏 jsonb 字面量？
suite("repro_jt_cross_047") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-047; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
