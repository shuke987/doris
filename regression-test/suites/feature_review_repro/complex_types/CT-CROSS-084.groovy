suite("repro_ct_cross_084") {
    def r = sql "SELECT element_at(CAST(NULL AS ARRAY<INT>), 1), array_size(CAST(NULL AS ARRAY<INT>)), array_contains(CAST(NULL AS ARRAY<INT>), 1)"
    assertEquals(null, r[0][0], "CT-CROSS-084a element_at NULL; observed=${r}")
    assertEquals(null, r[0][1], "CT-CROSS-084b array_size NULL; observed=${r}")
    assertEquals(null, r[0][2], "CT-CROSS-084c array_contains NULL; observed=${r}")
}
