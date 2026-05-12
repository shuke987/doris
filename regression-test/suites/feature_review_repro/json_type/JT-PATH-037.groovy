// JT-PATH-037: $.A vs $.a 区分
suite("repro_jt_path_037") {
    def r1 = sql "SELECT json_extract(CAST('{\"A\":1,\"a\":2}' AS JSONB), '\$.A')"
    def r2 = sql "SELECT json_extract(CAST('{\"A\":1,\"a\":2}' AS JSONB), '\$.a')"
    assertEquals('1', r1[0][0].toString(), "JT-PATH-037: \$.A=1; observed=${r1}")
    assertEquals('2', r2[0][0].toString(), "JT-PATH-037: \$.a=2; observed=${r2}")
}
