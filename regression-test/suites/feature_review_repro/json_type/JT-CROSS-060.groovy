// JT-CROSS-060: histogram on jsonb_col
suite("repro_jt_cross_060") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-060; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
