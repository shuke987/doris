suite("repro_ct_array_173") {
    def r = sql "SELECT array_size(array_slice(array(1,2,3), 1, 0))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-173: slice length=0; observed=${r}")
}
