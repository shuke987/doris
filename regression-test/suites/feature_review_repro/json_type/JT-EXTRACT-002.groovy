// JT-EXTRACT-002: 嵌套 path
suite("repro_jt_extract_002") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":{\"b\":2}}' AS JSONB), '\$.a.b')"
    assertEquals("2", r[0][0].toString(), "JT-EXTRACT-002; observed=${r}")
}
