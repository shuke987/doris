suite("repro_ct_array_132") {
    // CASE_FLAW fix: array_exists returns ARRAY; use array_count for boolean semantics
    def r = sql "SELECT array_count(x->x>100, array(1,2,3))"
    long n = (r[0][0] as Number).longValue()
    assertEquals(0L, n, "CT-ARRAY-132: none > 100, count=0; observed=${r}")
}
