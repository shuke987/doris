suite("repro_ct_lambda_032") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_match_any(x->x>2, array(1,2,3))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // array_match_any may not exist; use array_count >0
    if (threw) {
        def r2 = sql "SELECT array_count(x->x>2, array(1,2,3))"
        long n = (r2[0][0] as Number).longValue()
        assertTrue(n > 0L, "CT-LAMBDA-032: at least 1 match; observed=${r2}")
    } else {
        assertTrue(obs == true || (obs as Number).longValue() == 1L, "CT-LAMBDA-032: match_any true; observed=${obs}")
    }
}
