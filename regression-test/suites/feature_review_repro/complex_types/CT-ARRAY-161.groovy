suite("repro_ct_array_161") {
    def r = sql "SELECT array_sortby(x->-x, array(1,2,3))"
    assertEquals("[3, 2, 1]", r[0][0].toString(), "CT-ARRAY-161: array_sortby reverse; observed=${r}")
}
