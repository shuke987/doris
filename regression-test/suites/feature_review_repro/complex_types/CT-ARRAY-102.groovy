suite("repro_ct_array_102") {
    def r = sql "SELECT cardinality(array(1,2,3))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-102: cardinality alias; observed=${r}")
}
