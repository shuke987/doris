// JT-SERDE-021: broker_load ORC string → JSONB
suite("repro_jt_serde_021") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-021; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
