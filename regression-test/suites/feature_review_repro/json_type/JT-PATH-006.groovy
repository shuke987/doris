// JT-PATH-006: $[last]
suite("repro_jt_path_006") {
    def r = sql "SELECT jsonb_extract(CAST('[10,20,30]' AS JSONB), '\$[last]')"
    assertEquals("30", r[0][0].toString(), "JT-PATH-006; observed=${r}")
}
