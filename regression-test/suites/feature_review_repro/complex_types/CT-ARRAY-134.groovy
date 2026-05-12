suite("repro_ct_array_134") {
    def r = sql "SELECT array_exists(x->x>0, CAST(NULL AS ARRAY<INT>))"
    assertEquals(null, r[0][0], "CT-ARRAY-134: array_exists NULL array = NULL; observed=${r}")
}
