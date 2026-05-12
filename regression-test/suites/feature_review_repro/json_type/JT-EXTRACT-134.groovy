// JT-EXTRACT-134: jsonb_type NULL jsonb
suite("repro_jt_extract_134") {
    def r = sql "SELECT jsonb_type(NULL, '\$')"
    assertEquals(null, r[0][0], "JT-EXTRACT-134; observed=${r}")
}
