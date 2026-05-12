// CT-ARRAY-058: array_with_constant(5, 'x')
suite("repro_ct_array_058") {
    def r = sql "SELECT array_size(array_with_constant(5, 'x'))"
    assertEquals(5L, (r[0][0] as Number).longValue(), "CT-ARRAY-058: array_with_constant(5, 'x'); observed=${r}")
}
