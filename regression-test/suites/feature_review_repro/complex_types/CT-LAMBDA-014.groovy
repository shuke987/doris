suite("repro_ct_lambda_014") {
    def r = sql "SELECT array_map(x->array(x), array(1,2))"
    assertTrue(r[0][0] != null, "CT-LAMBDA-014: lambda returns array; observed=${r}")
}
