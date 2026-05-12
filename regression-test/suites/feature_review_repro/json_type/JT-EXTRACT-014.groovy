// JT-EXTRACT-014: 多 path 参数
suite("repro_jt_extract_014") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1,\"b\":2}' AS JSONB), '\$.a', '\$.b')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("2"), "JT-EXTRACT-014; observed=${r}")
}
