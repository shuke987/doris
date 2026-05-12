// CT-ARRAY-066: array_range(5)
suite("repro_ct_array_066") {
    def r = sql "SELECT array_size(array_range(5))"
    assertEquals(5L, (r[0][0] as Number).longValue(), "CT-ARRAY-066: array_range(5); observed=${r}")
}
