suite("repro_ct_lambda_023") {
    def r = sql "SELECT array_filter(x->x>0, CAST(NULL AS ARRAY<INT>))"
    assertEquals(null, r[0][0], "CT-LAMBDA-023: NULL filter; observed=${r}")
}
