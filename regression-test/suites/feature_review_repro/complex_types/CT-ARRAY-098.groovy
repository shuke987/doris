suite("repro_ct_array_098") {
    def r = sql "SELECT array_size(array(1,2,3))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-098: array_size=3; observed=${r}")
}
