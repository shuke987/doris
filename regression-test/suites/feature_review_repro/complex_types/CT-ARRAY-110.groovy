suite("repro_ct_array_110") {
    def r = sql "SELECT array_last_index(x->x>5, array(1,3,7,2,9))"
    assertEquals(5L, (r[0][0] as Number).longValue(), "CT-ARRAY-110: array_last_index >5 idx=5; observed=${r}")
}
