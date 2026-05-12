suite("repro_ct_lambda_017") {
    def r = sql "SELECT array_filter(array(1,2,3,4), array(true,false,true,false))"
    assertEquals("[1, 3]", r[0][0].toString(), "CT-LAMBDA-017: dual mask; observed=${r}")
}
