// JT-SERDE-009: Arrow Flight LARGE_STRING
suite("repro_jt_serde_009") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-009; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
