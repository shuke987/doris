// JT-PATH-039: 函数名大小写 `JSON_EXTRACT` vs `json_extract`
suite("repro_jt_path_039") {
    def r1 = sql "SELECT JSON_EXTRACT(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    def r2 = sql "SELECT json_extract(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals(r2[0][0]?.toString(), r1[0][0]?.toString(), "JT-PATH-039: function name case-insensitive")
}
