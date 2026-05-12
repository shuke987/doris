suite("repro_ct_array_130") {
    def r = sql "SELECT array_contains_all(array(1,2,3), array())"
    assertEquals(true, (r[0][0] as Boolean), "CT-ARRAY-130: empty subset = true; observed=${r}")
}
