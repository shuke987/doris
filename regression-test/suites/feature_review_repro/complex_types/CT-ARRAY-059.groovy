// CT-ARRAY-059: array_with_constant(0, 'x')
suite("repro_ct_array_059") {
    def r = sql "SELECT array_size(array_with_constant(0, 'x'))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-ARRAY-059: array_with_constant(0, 'x'); observed=${r}")
}
