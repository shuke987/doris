// JT-PATH-070: `$[last-99]` 和 `$[99]` 在 3-elem array
suite("repro_jt_path_070") {
    def r1 = sql "SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[last-99]')"
    def r2 = sql "SELECT json_extract(CAST('[1,2,3]' AS JSONB), '\$[99]')"
    assertEquals(null, r1[0][0], "JT-PATH-070: last-99 NULL; observed=${r1}")
    assertEquals(null, r2[0][0], "JT-PATH-070: [99] NULL; observed=${r2}")
}
