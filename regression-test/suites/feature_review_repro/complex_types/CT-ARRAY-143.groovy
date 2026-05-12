suite("repro_ct_array_143") {
    def r = sql "SELECT array_size(array_intersect(array(1,1,2), array(1,2,2)))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-143: intersect dedup; observed=${r}")
}
