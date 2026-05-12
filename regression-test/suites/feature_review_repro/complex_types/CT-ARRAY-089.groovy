suite("repro_ct_array_089") {
    def r = sql "SELECT element_at(array(), 1)"
    assertEquals(null, r[0][0], "CT-ARRAY-089: element_at([],1) -> NULL; observed=${r}")
}
