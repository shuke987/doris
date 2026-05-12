// JT-EXTRACT-109: jsonb_keys path 不存在
suite("repro_jt_extract_109") {
    def r = sql "SELECT jsonb_keys(CAST('{\"a\":1}' AS JSONB), '\$.nope')"
    assertEquals(null, r[0][0], "JT-EXTRACT-109; observed=${r}")
}
