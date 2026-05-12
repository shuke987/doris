suite("repro_ct_lambda_055") {
    def r = sql "SELECT array_filter(array(1,2,3), array(true, CAST(NULL AS BOOLEAN), true))"
    String s = r[0][0].toString()
    // spec: NULL -> false
    assertTrue(s.contains("1") && s.contains("3"), "CT-LAMBDA-055: NULL mask -> false; observed=${r}")
}
