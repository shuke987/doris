// JT-PATH-038: $.ABC vs $.abc 区分
suite("repro_jt_path_038") {
    def r1 = sql "SELECT json_extract(CAST('{\"ABC\":1,\"abc\":2}' AS JSONB), '\$.ABC')"
    def r2 = sql "SELECT json_extract(CAST('{\"ABC\":1,\"abc\":2}' AS JSONB), '\$.abc')"
    assertEquals('1', r1[0][0].toString(), "JT-PATH-038; observed=${r1}")
    assertEquals('2', r2[0][0].toString(), "JT-PATH-038; observed=${r2}")
}
