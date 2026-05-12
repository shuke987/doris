suite("repro_ct_lambda_020") {
    def r = sql "SELECT array_size(array_filter(x->false, array(1,2,3)))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-LAMBDA-020: all false=empty; observed=${r}")
}
