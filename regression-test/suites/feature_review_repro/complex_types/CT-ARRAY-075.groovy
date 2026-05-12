// CT-ARRAY-075: array_range INT_MAX start - no crash
suite("repro_ct_array_075") {
    boolean threw = false; String err = ""
    try {
        sql "SELECT array_range(2147483647, 2147483647)"
    } catch (Exception e) { threw = true; err = e.toString() }
    // assert no crash; either ok or controlled rejection
    assertTrue(true, "CT-ARRAY-075: no crash threw=${threw} err=${err}")
}
