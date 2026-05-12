suite("repro_ct_array_108") {
    def r = sql "SELECT array_position(array(), 1)"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-108: array_position([],1)=0; observed=${r}")
}
