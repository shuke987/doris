suite("repro_ct_array_133") {
    // CASE_FLAW fix: array_exists returns ARRAY; use array_count
    def r = sql "SELECT array_count(x->x>0, array())"
    long n = (r[0][0] as Number).longValue()
    assertEquals(0L, n, "CT-ARRAY-133: empty array count=0; observed=${r}")
}
