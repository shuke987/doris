// JT-EXTRACT-037: 同 path 多次 extract — 结果一致 (CSE 验证)
suite("repro_jt_extract_037") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":5}' AS JSONB), '\$.a'), jsonb_extract(CAST('{\"a\":5}' AS JSONB), '\$.a')"
    assertEquals("5", r[0][0].toString(), "JT-EXTRACT-037 col1; observed=${r}")
    assertEquals("5", r[0][1].toString(), "JT-EXTRACT-037 col2; observed=${r}")
}
