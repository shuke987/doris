// JT-PATH-043: $[2^31-1]
suite("repro_jt_path_043") {
    def r = sql """SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[2147483646]')"""
    assertEquals(null, r[0][0], "JT-PATH-043: expect NULL; observed=${r}")
}
