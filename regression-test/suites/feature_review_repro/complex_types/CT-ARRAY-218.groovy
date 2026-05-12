suite("repro_ct_array_218") {
    def r = sql "SELECT array_product(array(1,0,3))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-218: product with 0=0; observed=${r}")
}
