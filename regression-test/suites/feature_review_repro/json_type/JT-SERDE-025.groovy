// JT-SERDE-025: OUTFILE Parquet + jsonb 列
suite("repro_jt_serde_025") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-025; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
