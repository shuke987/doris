suite("repro_ct_array_129") {
    def r = sql "SELECT array_contains_all(array(1,2,3), array(1,4))"
    assertEquals(false, (r[0][0] as Boolean), "CT-ARRAY-129: contains_all false; observed=${r}")
}
