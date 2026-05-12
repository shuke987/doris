suite("repro_ct_lambda_030") {
    def r = sql "SELECT array_sortby(x->CAST(NULL AS INT), array(1,2,3))"
    assertTrue(r[0][0] != null, "CT-LAMBDA-030: NULL key sortby; observed=${r}")
}
