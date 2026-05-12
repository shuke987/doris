// JT-EXTRACT-062: extract_double 对 int
suite("repro_jt_extract_062") {
    def r = sql "SELECT jsonb_extract_double(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("1"), "JT-EXTRACT-062; observed=${r}")
}
