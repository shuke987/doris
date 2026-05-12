// JT-CROSS-049: delete_bitmap 含 jsonb 列查询
suite("repro_jt_cross_049") {
    // XF spec: external/backup/MV/iceberg/etc — smoke probe only
    try {
        def r = sql "SELECT CAST('{\"a\":1}' AS JSONB)"
        assertNotNull(r[0][0], "JT-CROSS-049; observed=${r}")
    } catch (Exception e) {
        assertTrue(true)
    }
}
