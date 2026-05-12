// JT-EXTRACT-001: top-level scalar
suite("repro_jt_extract_001") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-001; observed=${r}")
}
