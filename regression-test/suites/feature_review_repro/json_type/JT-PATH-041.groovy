// JT-PATH-041: $[last] 空数组
suite("repro_jt_path_041") {
    def r = sql """SELECT json_extract(CAST('[]' AS JSONB), '\$[last]')"""
    assertEquals(null, r[0][0], "JT-PATH-041: expect NULL; observed=${r}")
}
