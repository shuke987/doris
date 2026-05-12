// JT-EXTRACT-101: type path 不存在
suite("repro_jt_extract_101") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":1}' AS JSONB), '\$.nope')"
    assertEquals(null, r[0][0], "JT-EXTRACT-101; observed=${r}")
}
