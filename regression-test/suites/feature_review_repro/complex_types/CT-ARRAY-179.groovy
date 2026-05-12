suite("repro_ct_array_179") {
    def r = sql "SELECT array_concat(array(1,2), array(3,4))"
    assertEquals("[1, 2, 3, 4]", r[0][0].toString(), "CT-ARRAY-179: concat; observed=${r}")
}
