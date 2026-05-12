// JT-CROSS-001: JSONB × ARRAY 边界 — array → jsonb
suite("repro_jt_cross_001") {
    def r = sql "SELECT CAST(ARRAY(1,2,3) AS JSONB)"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("[") && v.contains("1"), "JT-CROSS-001; observed=${r}")
}
