suite("repro_ct_array_138") {
    def r = sql "SELECT array_size(array_distinct(array(NULL,1,NULL,2)))"
    long n = (r[0][0] as Number).longValue()
    // spec: NULL deduplicated to 1, or all preserved
    assertTrue(n == 3L || n == 4L, "CT-ARRAY-138: array_distinct NULL behavior; observed=${r}")
}
