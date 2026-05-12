suite("repro_ct_lambda_013") {
    def r = sql "SELECT array_map(x->CAST(x AS STRING), array(1,2,3))"
    String s = r[0][0].toString()
    assertTrue(s.contains("1") && s.contains("3"), "CT-LAMBDA-013: lambda returns string; observed=${r}")
}
