// JT-EXTRACT-061: extract_double 正向
suite("repro_jt_extract_061") {
    def r = sql "SELECT jsonb_extract_double(CAST('{\"a\":3.14}' AS JSONB), '\$.a')"
    String v = r[0][0].toString()
    assertTrue(v.startsWith("3.14"), "JT-EXTRACT-061; observed=${r}")
}
