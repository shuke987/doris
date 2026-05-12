suite("repro_ct_lambda_004") {
    def r = sql "SELECT array_map((x,y,z)->x+y+z, array(1,2), array(10,20), array(100,200))"
    assertEquals("[111, 222]", r[0][0].toString(), "CT-LAMBDA-004: 3 arrays; observed=${r}")
}
