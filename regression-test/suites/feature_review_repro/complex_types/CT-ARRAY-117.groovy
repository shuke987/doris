suite("repro_ct_array_117") {
    def r = sql "SELECT array_contains(array(), 1)"
    assertEquals(false, (r[0][0] as Boolean), "CT-ARRAY-117: empty array contains 1 = false; observed=${r}")
}
