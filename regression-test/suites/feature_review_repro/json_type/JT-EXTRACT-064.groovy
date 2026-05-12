// JT-EXTRACT-064: extract_double 对 string — spec: NULL
suite("repro_jt_extract_064") {
    def r = sql "SELECT jsonb_extract_double(CAST('{\"a\":\"3.14\"}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-064 (SEV): extract_double on string should NULL; observed=${r}")
}
