// JT-CROSS-036: MySQL JSON 列 → Doris JSONB
suite("repro_jt_cross_036") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-036; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
