suite("repro_ct_array_081") {
    def r = sql "SELECT element_at(array(1,2,3), 1)"
    assertEquals(1, (r[0][0] as Number).intValue(), "CT-ARRAY-081: element_at 1-based; observed=${r}")
}
