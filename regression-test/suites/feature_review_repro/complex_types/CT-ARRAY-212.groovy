suite("repro_ct_array_212") {
    def r = sql "SELECT array_min(array(3,1,2))"
    assertEquals(1, (r[0][0] as Number).intValue(), "CT-ARRAY-212: min=1; observed=${r}")
}
