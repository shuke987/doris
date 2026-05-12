suite("repro_ct_lambda_027") {
    def r = sql "SELECT array_sortby(x->-x, array(3,1,2))"
    assertEquals("[3, 2, 1]", r[0][0].toString(), "CT-LAMBDA-027: sortby -x; observed=${r}")
}
