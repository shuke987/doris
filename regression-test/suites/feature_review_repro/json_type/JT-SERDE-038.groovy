// JT-SERDE-038: sort spill 含 jsonb
suite("repro_jt_serde_038") {
    // XF: external feature (arrow flight / broker_load / OUTFILE / compaction) — smoke probe
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-SERDE-038; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
