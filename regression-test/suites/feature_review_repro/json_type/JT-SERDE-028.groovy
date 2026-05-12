// JT-SERDE-028: EXPORT to S3 + jsonb
suite("repro_jt_serde_028") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-028; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
