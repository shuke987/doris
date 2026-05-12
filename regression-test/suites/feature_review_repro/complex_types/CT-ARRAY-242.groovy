suite("repro_ct_array_242") {
    def r = sql "SELECT element_at(array('a','b','c'), 0)"
    assertEquals(null, r[0][0], "CT-ARRAY-242: idx 0 string version -> NULL; observed=${r}")
}
