// JT-PATH-002: $.a simple key
suite("repro_jt_path_002") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r[0][0].toString(), "JT-PATH-002; observed=${r}")
}
