suite("repro_ct_array_135") {
    def r = sql "SELECT array_count(x->x>2, array(1,2,3,4))"
    assertEquals(2L, (r[0][0] as Number).longValue(), "CT-ARRAY-135: array_count = 2; observed=${r}")
}
