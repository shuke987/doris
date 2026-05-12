// JT-EXTRACT-050: extract_int 对 string "1" — spec: 深 cast 不强转 → NULL
// 实际行为：cluster 返回 1 = SEV 复现
suite("repro_jt_extract_050") {
    try {
        def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":\"1\"}' AS JSONB), '\$.a')"
        assertEquals(null, r[0][0],
            "JT-EXTRACT-050 (SEV): extract_int on string value should return NULL; observed=${r}")
    } catch (Exception e) {
        if (e.message?.contains('Can not found function') || e.message?.contains('Unsupported')) {
            logger.info("JT-EXTRACT-050: function unavailable: ${e.message}")
        } else {
            throw e
        }
    }
}
