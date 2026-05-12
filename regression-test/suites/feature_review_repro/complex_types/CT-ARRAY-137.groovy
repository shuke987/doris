suite("repro_ct_array_137") {
    def r = sql "SELECT array_size(array_distinct(array(1,2,2,3,3,3)))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-137: array_distinct size=3; observed=${r}")
}
