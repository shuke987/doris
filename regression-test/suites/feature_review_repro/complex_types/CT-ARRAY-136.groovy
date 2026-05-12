suite("repro_ct_array_136") {
    def r = sql "SELECT array_count(x->x>0, array())"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-136: array_count empty = 0; observed=${r}")
}
