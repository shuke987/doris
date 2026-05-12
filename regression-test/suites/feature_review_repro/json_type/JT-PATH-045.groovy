// JT-PATH-045: $[last-1] 单元素
suite("repro_jt_path_045") {
    def r = sql """SELECT json_extract(CAST('[1]' AS JSONB), '\$[last-1]')"""
    assertEquals(null, r[0][0], "JT-PATH-045: expect NULL; observed=${r}")
}
