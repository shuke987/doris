suite("repro_ct_array_215") {
    def r = sql "SELECT array_max(array(1,3,2))"
    assertEquals(3, (r[0][0] as Number).intValue(), "CT-ARRAY-215: max=3; observed=${r}")
}
