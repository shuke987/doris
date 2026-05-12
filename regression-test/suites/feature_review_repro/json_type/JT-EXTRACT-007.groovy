// JT-EXTRACT-007: path 不存在 → NULL
suite("repro_jt_extract_007") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.nope')"
    assertEquals(null, r[0][0], "JT-EXTRACT-007; observed=${r}")
}
