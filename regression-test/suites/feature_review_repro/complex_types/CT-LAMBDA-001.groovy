suite("repro_ct_lambda_001") {
    def r = sql "SELECT array_map(x->x+1, array(1,2,3))"
    assertEquals("[2, 3, 4]", r[0][0].toString(), "CT-LAMBDA-001: array_map +1; observed=${r}")
}
