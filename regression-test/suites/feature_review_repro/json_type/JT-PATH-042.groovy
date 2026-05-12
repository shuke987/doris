// JT-PATH-042: $[0] 单元素
suite("repro_jt_path_042") {
    def r = sql """SELECT json_extract(CAST('[1]' AS JSONB), '\$[0]')"""
    assertEquals('1', r[0][0]?.toString(), "JT-PATH-042; observed=${r}")
}
