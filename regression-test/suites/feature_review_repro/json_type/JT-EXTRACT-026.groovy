// JT-EXTRACT-026: path 大小写区分 → 不匹配
suite("repro_jt_extract_026") {
    def r = sql "SELECT jsonb_extract(CAST('{\"A\":1}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-026; observed=${r}")
}
