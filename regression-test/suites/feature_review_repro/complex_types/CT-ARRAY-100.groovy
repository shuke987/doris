suite("repro_ct_array_100") {
    def r = sql "SELECT array_size(CAST(NULL AS ARRAY<INT>))"
    assertEquals(null, r[0][0], "CT-ARRAY-100: array_size(NULL)=NULL; observed=${r}")
}
