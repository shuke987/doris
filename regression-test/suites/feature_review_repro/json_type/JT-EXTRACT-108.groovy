// JT-EXTRACT-108: jsonb_keys NULL jsonb
suite("repro_jt_extract_108") {
    def r = sql "SELECT jsonb_keys(NULL)"
    assertEquals(null, r[0][0], "JT-EXTRACT-108; observed=${r}")
}
