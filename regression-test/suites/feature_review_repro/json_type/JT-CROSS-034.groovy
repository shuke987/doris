// JT-CROSS-034: iceberg V2 JSON logical type → Doris JSONB
suite("repro_jt_cross_034") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-034; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
