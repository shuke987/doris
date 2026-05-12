suite("repro_ct_lambda_035") {
    def r = sql "SELECT array_count(x->x>0, CAST(NULL AS ARRAY<INT>))"
    Object obs = r[0][0]
    // NULL or 0
    assertTrue(obs == null || (obs as Number).longValue() == 0L, "CT-LAMBDA-035: NULL count; observed=${r}")
}
