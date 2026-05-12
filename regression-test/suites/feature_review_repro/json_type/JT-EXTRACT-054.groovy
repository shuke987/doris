// JT-EXTRACT-054: extract_int int128 over → NULL
suite("repro_jt_extract_054") {
    def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":12345678901234567890}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-054: int overflow → NULL; observed=${r}")
}
