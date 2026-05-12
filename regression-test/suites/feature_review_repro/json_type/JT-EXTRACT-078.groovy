// JT-EXTRACT-078: isnull path 不存在
suite("repro_jt_extract_078") {
    def r = sql "SELECT json_extract_isnull(CAST('{\"a\":1}' AS JSONB), '\$.nope')"
    assertEquals(null, r[0][0], "JT-EXTRACT-078; observed=${r}")
}
