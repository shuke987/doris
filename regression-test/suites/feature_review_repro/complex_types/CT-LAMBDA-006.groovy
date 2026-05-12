suite("repro_ct_lambda_006") {
    def r = sql "SELECT array_map(x->x+1, CAST(NULL AS ARRAY<INT>))"
    assertEquals(null, r[0][0], "CT-LAMBDA-006: NULL array; observed=${r}")
}
