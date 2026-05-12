suite("repro_ct_array_223") {
    def r = sql "SELECT array_size(array_difference(array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-223: difference empty=[]; observed=${r}")
}
