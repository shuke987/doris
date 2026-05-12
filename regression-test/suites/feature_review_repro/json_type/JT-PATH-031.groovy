// JT-PATH-031: $[last-99] under
suite("repro_jt_path_031") {
    def r = sql """SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[last-99]')"""
    assertEquals(null, r[0][0], "JT-PATH-031: expect NULL; observed=${r}")
}
