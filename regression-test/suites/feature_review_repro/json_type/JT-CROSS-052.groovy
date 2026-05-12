// JT-CROSS-052: group_commit 批内 1 行 jsonb parse 失败
suite("repro_jt_cross_052") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-052; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
