suite("repro_ct_array_210") {
    def r = sql "SELECT array_avg(array())"
    assertEquals(null, r[0][0], "CT-ARRAY-210: avg empty=NULL; observed=${r}")
}
