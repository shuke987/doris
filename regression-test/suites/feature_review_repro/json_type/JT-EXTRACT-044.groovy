// JT-EXTRACT-044: extract_string path 不存在
suite("repro_jt_extract_044") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":\"hi\"}' AS JSONB), '\$.nope')"
    assertEquals(null, r[0][0], "JT-EXTRACT-044; observed=${r}")
}
