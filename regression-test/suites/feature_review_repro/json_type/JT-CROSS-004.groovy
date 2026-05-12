// JT-CROSS-004: JSONB × CASE WHEN
suite("repro_jt_cross_004") {
    def r = sql "SELECT CASE WHEN 1=1 THEN CAST('{\"a\":1}' AS JSONB) ELSE CAST('{}' AS JSONB) END"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"), "JT-CROSS-004; observed=${r}")
}
