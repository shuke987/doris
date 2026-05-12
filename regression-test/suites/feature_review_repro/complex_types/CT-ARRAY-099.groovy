suite("repro_ct_array_099") {
    def r = sql "SELECT array_size(array())"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-099: array_size(empty)=0; observed=${r}")
}
