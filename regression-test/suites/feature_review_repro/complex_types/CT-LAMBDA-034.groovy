suite("repro_ct_lambda_034") {
    def r = sql "SELECT array_count(x->x>0, array())"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-LAMBDA-034: empty match; observed=${r}")
}
