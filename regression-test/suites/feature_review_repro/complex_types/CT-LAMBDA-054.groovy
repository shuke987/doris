suite("repro_ct_lambda_054") {
    def r1 = sql "SELECT array_filter(CAST(NULL AS ARRAY<INT>), array(true,false,true))"
    def r2 = sql "SELECT array_filter(array(1,2,3), CAST(NULL AS ARRAY<BOOLEAN>))"
    assertEquals(null, r1[0][0], "CT-LAMBDA-054a: NULL first; observed=${r1}")
    assertEquals(null, r2[0][0], "CT-LAMBDA-054b: NULL second; observed=${r2}")
}
