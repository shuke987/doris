suite("repro_ct_lambda_009") {
    def r = sql "SELECT array_map(x->x*2, array(1,CAST(NULL AS INT),3))"
    String s = r[0][0].toString()
    assertTrue(s.contains("2") && s.contains("6") && s.contains("null"), "CT-LAMBDA-009: NULL element preserves; observed=${r}")
}
