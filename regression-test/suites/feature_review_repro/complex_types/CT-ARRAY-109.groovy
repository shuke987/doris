suite("repro_ct_array_109") {
    def r = sql "SELECT array_first_index(x->x>5, array(1,3,7,2,9))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-109: array_first_index >5 idx=3; observed=${r}")
}
