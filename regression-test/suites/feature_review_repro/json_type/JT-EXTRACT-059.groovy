// JT-EXTRACT-059: extract_largeint 正向
suite("repro_jt_extract_059") {
    def r = sql "SELECT jsonb_extract_largeint(CAST('{\"a\":12345}' AS JSONB), '\$.a')"
    assertEquals("12345", r[0][0].toString(), "JT-EXTRACT-059; observed=${r}")
}
