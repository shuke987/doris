suite("repro_ct_array_176") {
    def r = sql "SELECT array_remove(array(1,2,3,2), 2)"
    assertEquals("[1, 3]", r[0][0].toString(), "CT-ARRAY-176: remove 2; observed=${r}")
}
