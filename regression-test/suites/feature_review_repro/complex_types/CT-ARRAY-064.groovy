// CT-ARRAY-064: array_with_constant(3, NULL)
suite("repro_ct_array_064") {
    def r = sql "SELECT array_size(array_with_constant(3, NULL))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-ARRAY-064: array_with_constant(3, NULL); observed=${r}")
}
