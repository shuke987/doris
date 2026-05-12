suite("repro_ct_array_168") {
    // CASE_FLAW fix: function name is `reverse`
    def r = sql "SELECT array_size(reverse(array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-168: reverse empty; observed=${r}")
}
