suite("repro_ct_array_191") {
    def r = sql "SELECT array_popfront(array(1,2,3))"
    assertEquals("[2, 3]", r[0][0].toString(), "CT-ARRAY-191: popfront; observed=${r}")
}
