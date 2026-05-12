suite("repro_ct_array_113") {
    def r = sql "SELECT array_contains(array(1,2,3), 99)"
    assertEquals(false, (r[0][0] as Boolean), "CT-ARRAY-113: array_contains 99 = false; observed=${r}")
}
