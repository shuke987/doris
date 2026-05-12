suite("repro_ct_array_088") {
    def r = sql "SELECT element_at(CAST(NULL AS ARRAY<INT>), 1)"
    assertEquals(null, r[0][0], "CT-ARRAY-088: element_at NULL array -> NULL; observed=${r}")
}
