suite("repro_ct_lambda_051") {
    def r = sql "SELECT array_size(array_map((x,y)->x+y, array(), array()))"
    assertEquals(0L, (r[0][0] as Number).longValue(), "CT-LAMBDA-051: dual empty; observed=${r}")
}
