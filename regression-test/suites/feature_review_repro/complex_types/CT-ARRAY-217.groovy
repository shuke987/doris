suite("repro_ct_array_217") {
    def r = sql "SELECT array_product(array(1,2,3))"
    Object obs = r[0][0]
    assertTrue(obs != null && (obs as Number).longValue() == 6L, "CT-ARRAY-217: product=6; observed=${r}")
}
