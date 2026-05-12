// JT-PATH-007: $[last-1]
suite("repro_jt_path_007") {
    def r = sql "SELECT jsonb_extract(CAST('[10,20,30]' AS JSONB), '\$[last-1]')"
    assertEquals("20", r[0][0].toString(), "JT-PATH-007; observed=${r}")
}
