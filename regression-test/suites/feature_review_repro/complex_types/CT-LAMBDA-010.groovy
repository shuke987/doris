suite("repro_ct_lambda_010") {
    def r = sql "SELECT array_map(x->array_map(y->y+1, x), array(array(1,2),array(3)))"
    String s = r[0][0].toString()
    assertTrue(s.contains("2") && s.contains("3") && s.contains("4"), "CT-LAMBDA-010: nested lambda; observed=${r}")
}
