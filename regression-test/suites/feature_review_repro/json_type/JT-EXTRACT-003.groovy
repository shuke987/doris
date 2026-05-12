// JT-EXTRACT-003: 数组索引
suite("repro_jt_extract_003") {
    def r = sql "SELECT jsonb_extract(CAST('[10,20,30]' AS JSONB), '\$[1]')"
    assertEquals("20", r[0][0].toString(), "JT-EXTRACT-003; observed=${r}")
}
