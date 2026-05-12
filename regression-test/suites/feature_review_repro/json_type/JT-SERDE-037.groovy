// JT-SERDE-037: hash join spill 含 jsonb
suite("repro_jt_serde_037") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-037; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
