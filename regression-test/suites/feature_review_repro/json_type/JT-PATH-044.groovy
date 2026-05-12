// JT-PATH-044: $[last-0]
suite("repro_jt_path_044") {
    def r = sql """SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[last-0]')"""
    assertEquals('3', r[0][0]?.toString(), "JT-PATH-044; observed=${r}")
}
