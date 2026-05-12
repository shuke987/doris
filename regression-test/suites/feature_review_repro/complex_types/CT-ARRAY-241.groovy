suite("repro_ct_array_241") {
    def r1 = sql "SELECT element_at(array(1,NULL,3), -2)"
    def r2 = sql "SELECT element_at(array(1,NULL,3), -1)"
    // -2 -> NULL element; -1 -> 3
    assertEquals(null, r1[0][0], "CT-ARRAY-241a: -2 returns nested NULL; observed=${r1}")
    assertEquals(3, (r2[0][0] as Number).intValue(), "CT-ARRAY-241b: -1 returns 3; observed=${r2}")
}
