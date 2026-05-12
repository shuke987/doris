suite("repro_ct_array_147") {
    def r = sql "SELECT array_size(array_union(array(1,2,3), array(3,4,5)))"
    assertEquals(5L, (r[0][0] as Number).longValue(), "CT-ARRAY-147: union size=5; observed=${r}")
}
