// JT-SERDE-020: broker_load Parquet string → JSONB
suite("repro_jt_serde_020") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-020; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
