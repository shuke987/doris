suite("repro_ct_array_087") {
    def r = sql "SELECT element_at(array(1,2,3), CAST(NULL AS INT))"
    assertEquals(null, r[0][0], "CT-ARRAY-087: element_at(NULL idx) -> NULL; observed=${r}")
}
