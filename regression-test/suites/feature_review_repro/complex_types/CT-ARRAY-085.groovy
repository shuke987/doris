suite("repro_ct_array_085") {
    def r = sql "SELECT element_at(array(1,2,3), 4)"
    assertEquals(null, r[0][0], "CT-ARRAY-085: element_at out-of-range -> NULL; observed=${r}")
}
