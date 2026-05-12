suite("repro_ct_lambda_042") {
    def r = sql "SELECT array_map(arr2 -> array_map(x->x+1, arr2), array(array(1,2), array(3)))"
    assertTrue(r[0][0] != null, "CT-LAMBDA-042: nested lambda; observed=${r}")
}
