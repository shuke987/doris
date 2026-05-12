suite("repro_ct_lambda_022") {
    def r = sql "SELECT array_size(array_filter(x->x>0, array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-LAMBDA-022: empty filter; observed=${r}")
}
