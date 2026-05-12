suite("repro_ct_array_112") {
    def r = sql "SELECT array_contains(array(1,2,3), 2)"
    assertEquals(true, (r[0][0] as Boolean), "CT-ARRAY-112: array_contains 2 = true; observed=${r}")
}
