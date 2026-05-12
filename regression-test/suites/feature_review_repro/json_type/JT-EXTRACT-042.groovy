// JT-EXTRACT-042: extract_string 对 array → NULL
suite("repro_jt_extract_042") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":[1,2]}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-042: extract_string on array → NULL; observed=${r}")
}
