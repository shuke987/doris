// JT-EXTRACT-011: $[last-1]
suite("repro_jt_extract_011") {
    def r = sql "SELECT jsonb_extract(CAST('[1,2,3]' AS JSONB), '\$[last-1]')"
    assertEquals("2", r[0][0].toString(), "JT-EXTRACT-011; observed=${r}")
}
