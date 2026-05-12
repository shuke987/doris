suite("repro_ct_array_116") {
    def r = sql "SELECT array_contains(CAST(NULL AS ARRAY<INT>), 1)"
    assertEquals(null, r[0][0], "CT-ARRAY-116: array_contains(NULL,1)=NULL; observed=${r}")
}
