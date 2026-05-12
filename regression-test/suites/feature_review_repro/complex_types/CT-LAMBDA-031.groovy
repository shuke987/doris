suite("repro_ct_lambda_031") {
    def r = sql "SELECT array_size(array_sortby(x->x, array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-LAMBDA-031: empty sortby; observed=${r}")
}
