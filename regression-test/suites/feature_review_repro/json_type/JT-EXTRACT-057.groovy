// JT-EXTRACT-057: extract_bigint 正向
suite("repro_jt_extract_057") {
    def r = sql "SELECT jsonb_extract_bigint(CAST('{\"a\":12345}' AS JSONB), '\$.a')"
    assertEquals("12345", r[0][0].toString(), "JT-EXTRACT-057; observed=${r}")
}
