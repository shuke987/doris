// JT-PATH-003: $.a.b 多层
suite("repro_jt_path_003") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":{\"b\":2}}' AS JSONB), '\$.a.b')"
    assertEquals("2", r[0][0].toString(), "JT-PATH-003; observed=${r}")
}
