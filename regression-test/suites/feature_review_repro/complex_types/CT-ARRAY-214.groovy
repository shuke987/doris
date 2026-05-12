suite("repro_ct_array_214") {
    def r = sql "SELECT array_min(array())"
    assertEquals(null, r[0][0], "CT-ARRAY-214: empty min=NULL; observed=${r}")
}
