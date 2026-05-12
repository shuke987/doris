// JT-EXTRACT-010: $[last]
suite("repro_jt_extract_010") {
    def r = sql "SELECT jsonb_extract(CAST('[1,2,3]' AS JSONB), '\$[last]')"
    assertEquals("3", r[0][0].toString(), "JT-EXTRACT-010; observed=${r}")
}
