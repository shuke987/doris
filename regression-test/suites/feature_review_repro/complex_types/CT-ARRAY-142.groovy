suite("repro_ct_array_142") {
    def r = sql "SELECT array_size(array_intersect(array(1,2,3), array(2,3,4)))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-142: intersect size=2; observed=${r}")
}
