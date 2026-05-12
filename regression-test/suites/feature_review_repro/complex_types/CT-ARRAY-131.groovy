suite("repro_ct_array_131") {
    // CASE_FLAW fix: array_exists in Doris returns ARRAY of per-element bool flags,
    // not a single boolean. Use array_count to test "at least one elem matches".
    def r = sql "SELECT array_count(x->x>2, array(1,2,3))"
    long n = (r[0][0] as Number).longValue()
    assertTrue(n >= 1L, "CT-ARRAY-131: at least one elem >2 exists; count=${n} observed=${r}")
}
