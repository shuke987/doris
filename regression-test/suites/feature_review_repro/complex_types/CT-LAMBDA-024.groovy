suite("repro_ct_lambda_024") {
    def r = sql "SELECT array_filter(array(1,2,3), array(true, CAST(NULL AS BOOLEAN), true))"
    Object obs = r[0][0]
    // spec: NULL as false or preserve
    assertTrue(obs != null, "CT-LAMBDA-024: mask NULL; observed=${r}")
}
