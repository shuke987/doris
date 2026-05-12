suite("repro_ct_array_127") {
    def r = sql "SELECT arrays_overlap(CAST(NULL AS ARRAY<INT>), array(1))"
    assertEquals(null, r[0][0], "CT-ARRAY-127: NULL overlap = NULL; observed=${r}")
}
