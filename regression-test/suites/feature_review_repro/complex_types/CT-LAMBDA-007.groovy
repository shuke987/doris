suite("repro_ct_lambda_007") {
    def r = sql "SELECT array_map(x->NULL, array(1,2))"
    String s = r[0][0].toString()
    assertTrue(s.contains("null"), "CT-LAMBDA-007: lambda returns NULL; observed=${r}")
}
