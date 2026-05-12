suite("repro_ct_array_170") {
    def r = sql "SELECT array_slice(array(1,2,3,4,5), 2, 2)"
    assertEquals("[2, 3]", r[0][0].toString(), "CT-ARRAY-170: slice; observed=${r}")
}
