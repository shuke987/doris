suite("repro_ct_array_216") {
    def r = sql "SELECT array_max(array('b','a','c'))"
    assertEquals("c", r[0][0].toString(), "CT-ARRAY-216: max string=c; observed=${r}")
}
