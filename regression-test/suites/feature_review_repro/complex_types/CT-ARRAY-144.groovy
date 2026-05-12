suite("repro_ct_array_144") {
    def r = sql "SELECT array_size(array_intersect(array(1,2,3), array(2,3,4), array(2,5)))"
    assertEquals(1L, (r[0][0] as Number).longValue(), "CT-ARRAY-144: intersect 3 arrays; observed=${r}")
}
