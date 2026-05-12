suite("repro_ct_lambda_026") {
    def r = sql "SELECT array_sortby(x->x, array(3,1,2))"
    assertEquals("[1, 2, 3]", r[0][0].toString(), "CT-LAMBDA-026: sortby identity; observed=${r}")
}
