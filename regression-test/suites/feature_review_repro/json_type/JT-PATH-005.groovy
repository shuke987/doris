// JT-PATH-005: $[0] 数组首
suite("repro_jt_path_005") {
    def r = sql "SELECT jsonb_extract(CAST('[10,20,30]' AS JSONB), '\$[0]')"
    assertEquals("10", r[0][0].toString(), "JT-PATH-005; observed=${r}")
}
