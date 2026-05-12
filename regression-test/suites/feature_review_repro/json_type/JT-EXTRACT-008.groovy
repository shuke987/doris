// JT-EXTRACT-008: nested path 不存在
suite("repro_jt_extract_008") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":{\"b\":1}}' AS JSONB), '\$.x.y')"
    assertEquals(null, r[0][0], "JT-EXTRACT-008; observed=${r}")
}
