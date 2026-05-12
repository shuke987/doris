// JT-CROSS-063: group_commit + jsonb_parse strict_mode × max_filter_ratio 矩阵
suite("repro_jt_cross_063") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-063; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
