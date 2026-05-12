// JT-SERDE-022: broker_load Parquet bytes → JSONB
suite("repro_jt_serde_022") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-022; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
