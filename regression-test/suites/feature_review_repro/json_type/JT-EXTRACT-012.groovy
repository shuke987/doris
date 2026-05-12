// JT-EXTRACT-012: $[last-99] 超头 → NULL
suite("repro_jt_extract_012") {
    def r = sql "SELECT jsonb_extract(CAST('[1,2,3]' AS JSONB), '\$[last-99]')"
    assertEquals(null, r[0][0], "JT-EXTRACT-012; observed=${r}")
}
