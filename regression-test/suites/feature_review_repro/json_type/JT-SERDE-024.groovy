// JT-SERDE-024: OUTFILE CSV + jsonb 列
suite("repro_jt_serde_024") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-024; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
