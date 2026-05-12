// JT-PATH-040: $[0] 空数组
suite("repro_jt_path_040") {
    def r = sql """SELECT json_extract(CAST('[]' AS JSONB), '\$[0]')"""
    assertEquals(null, r[0][0], "JT-PATH-040: expect NULL; observed=${r}")
}
