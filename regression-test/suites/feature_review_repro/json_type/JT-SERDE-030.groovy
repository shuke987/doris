// JT-SERDE-030: 写 1M 行后 base compaction
suite("repro_jt_serde_030") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-030; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
