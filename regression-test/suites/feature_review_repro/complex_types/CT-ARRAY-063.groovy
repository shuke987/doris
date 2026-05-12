// CT-ARRAY-063: array_with_constant(NULL, 'x') -> NULL
suite("repro_ct_array_063") {
    def r = sql "SELECT array_with_constant(NULL, 'x')"
    assertEquals(null, r[0][0], "CT-ARRAY-063: array_with_constant(NULL, 'x') -> NULL; observed=${r}")
}
