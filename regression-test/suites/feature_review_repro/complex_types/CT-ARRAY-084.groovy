suite("repro_ct_array_084") {
    def r = sql "SELECT element_at(array(1,2,3), 0)"
    assertEquals(null, r[0][0], "CT-ARRAY-084: element_at(0) -> NULL; observed=${r}")
}
