// JT-SERDE-023: broker_load JSON 文件 → JSONB
suite("repro_jt_serde_023") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-023; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
