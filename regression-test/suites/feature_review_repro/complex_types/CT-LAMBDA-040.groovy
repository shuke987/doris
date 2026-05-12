suite("repro_ct_lambda_040") {
    def r = sql "SELECT array_map(x->x+1, array(1,2))"
    assertTrue(r[0][0] != null, "CT-LAMBDA-040: closure const; observed=${r}")
}
