suite("repro_ct_lambda_021") {
    def r = sql "SELECT array_size(array_filter(x->true, array(1,2,3)))"
    assertEquals(3L, (r[0][0] as Number).longValue(), "CT-LAMBDA-021: all true=original; observed=${r}")
}
