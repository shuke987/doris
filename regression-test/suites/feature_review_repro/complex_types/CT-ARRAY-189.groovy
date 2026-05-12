suite("repro_ct_array_189") {
    def r = sql "SELECT array_popback(array(1,2,3))"
    assertEquals("[1, 2]", r[0][0].toString(), "CT-ARRAY-189: popback; observed=${r}")
}
