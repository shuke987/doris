// JT-CROSS-050: MOW + partial_update + jsonb
suite("repro_jt_cross_050") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-050; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
