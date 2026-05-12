// JT-EXTRACT-009: 数组越界 → NULL
suite("repro_jt_extract_009") {
    def r = sql "SELECT jsonb_extract(CAST('[1,2,3]' AS JSONB), '\$[10]')"
    assertEquals(null, r[0][0], "JT-EXTRACT-009; observed=${r}")
}
