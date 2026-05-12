suite("repro_ct_lambda_033") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_match_all(x->x>2, array(1,2,3))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    if (threw) {
        def r2 = sql "SELECT array_count(x->x>2, array(1,2,3)), array_size(array(1,2,3))"
        long n = (r2[0][0] as Number).longValue()
        long sz = (r2[0][1] as Number).longValue()
        assertTrue(n != sz, "CT-LAMBDA-033: not all match; observed=${r2}")
    } else {
        assertTrue(obs == false || (obs as Number).longValue() == 0L, "CT-LAMBDA-033: match_all false; observed=${obs}")
    }
}
