// JT-CROSS-005: JSONB × COALESCE
suite("repro_jt_cross_005") {
    def r = sql "SELECT COALESCE(CAST(NULL AS JSONB), CAST('[1,2]' AS JSONB))"
    assertEquals("[1,2]", r[0][0].toString(), "JT-CROSS-005; observed=${r}")
}
