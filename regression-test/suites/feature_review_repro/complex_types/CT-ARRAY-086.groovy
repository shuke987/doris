suite("repro_ct_array_086") {
    def r = sql "SELECT element_at(array(1,2,3), -10)"
    assertEquals(null, r[0][0], "CT-ARRAY-086: element_at(-10) -> NULL; observed=${r}")
}
