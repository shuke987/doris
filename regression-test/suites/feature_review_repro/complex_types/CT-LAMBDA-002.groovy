suite("repro_ct_lambda_002") {
    def r = sql "SELECT array_map((x,y)->x+y, array(1,2), array(10,20))"
    assertEquals("[11, 22]", r[0][0].toString(), "CT-LAMBDA-002: 2 arrays; observed=${r}")
}
