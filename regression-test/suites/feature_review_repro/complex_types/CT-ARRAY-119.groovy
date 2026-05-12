suite("repro_ct_array_119") {
    def r = sql "SELECT array_contains(array(1.0, 2.0), 1)"
    assertEquals(true, (r[0][0] as Boolean), "CT-ARRAY-119: type promote 1.0 contains 1; observed=${r}")
}
