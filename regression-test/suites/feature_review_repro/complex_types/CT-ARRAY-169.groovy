suite("repro_ct_array_169") {
    // CASE_FLAW fix: function name is `reverse`
    def r = sql "SELECT reverse(CAST(NULL AS ARRAY<INT>))"
    assertEquals(null, r[0][0], "CT-ARRAY-169: reverse NULL; observed=${r}")
}
