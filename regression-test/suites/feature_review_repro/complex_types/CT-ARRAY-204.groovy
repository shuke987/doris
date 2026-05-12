suite("repro_ct_array_204") {
    def r = sql "SELECT array_sum(array(1,2,3))"
    assertEquals(6L, (r[0][0] as Number).longValue(), "CT-ARRAY-204: sum=6; observed=${r}")
}
