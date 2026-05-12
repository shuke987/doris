// JT-PATH-004: $.a.b.c.d.e 5 层
suite("repro_jt_path_004") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":{\"b\":{\"c\":{\"d\":{\"e\":99}}}}}' AS JSONB), '\$.a.b.c.d.e')"
    assertEquals("99", r[0][0].toString(), "JT-PATH-004; observed=${r}")
}
