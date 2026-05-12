// JT-EXTRACT-015: 多 path 全不存在
suite("repro_jt_extract_015") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.x', '\$.y')"
    // expect: array of NULL OR NULL itself; document observed behaviour
    assertNotNull(r, "JT-EXTRACT-015; observed=${r}")
}
