suite("repro_ct_lambda_016") {
    def r = sql "SELECT array_filter(x->x>2, array(1,2,3,4))"
    assertEquals("[3, 4]", r[0][0].toString(), "CT-LAMBDA-016: filter >2; observed=${r}")
}
