// CT-ARRAY-068: array_range(1, 10, 2)
suite("repro_ct_array_068") {
    def r = sql "SELECT array_size(array_range(1, 10, 2))"
    assertEquals(5L, (r[0][0] as Number).longValue(), "CT-ARRAY-068: array_range(1, 10, 2); observed=${r}")
}
