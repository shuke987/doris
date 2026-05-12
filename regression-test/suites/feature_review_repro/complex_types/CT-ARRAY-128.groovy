suite("repro_ct_array_128") {
    def r = sql "SELECT array_contains_all(array(1,2,3), array(1,2))"
    assertEquals(true, (r[0][0] as Boolean), "CT-ARRAY-128: contains_all=true; observed=${r}")
}
