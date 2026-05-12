suite("repro_ct_lambda_005") {
    def r = sql "SELECT array_size(array_map(x->x+1, array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-LAMBDA-005: empty array_map; observed=${r}")
}
