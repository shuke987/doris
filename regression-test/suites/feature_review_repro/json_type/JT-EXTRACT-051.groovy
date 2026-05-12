// JT-EXTRACT-051: extract_int 对 string "abc" — 应 NULL
suite("repro_jt_extract_051") {
    def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":\"abc\"}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-051: extract_int on non-numeric string → NULL; observed=${r}")
}
