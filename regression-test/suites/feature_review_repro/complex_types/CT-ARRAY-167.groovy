suite("repro_ct_array_167") {
    // CASE_FLAW fix: function name is `reverse` not `array_reverse`
    def r = sql "SELECT reverse(array(1,2,3))"
    assertEquals("[3, 2, 1]", r[0][0].toString(), "CT-ARRAY-167: reverse; observed=${r}")
}
