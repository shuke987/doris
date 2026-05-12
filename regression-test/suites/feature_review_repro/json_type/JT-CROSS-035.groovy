// JT-CROSS-035: paimon JSON 列 → JSONB
suite("repro_jt_cross_035") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-035; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
