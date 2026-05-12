suite("repro_ct_array_140") {
    def r = sql "SELECT array_distinct(CAST(NULL AS ARRAY<INT>))"
    assertEquals(null, r[0][0], "CT-ARRAY-140: distinct NULL; observed=${r}")
}
