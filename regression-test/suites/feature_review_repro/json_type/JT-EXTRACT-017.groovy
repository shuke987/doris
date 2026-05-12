// JT-EXTRACT-017: NULL jsonb input → NULL
suite("repro_jt_extract_017") {
    def r = sql "SELECT jsonb_extract(NULL, '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-017; observed=${r}")
}
