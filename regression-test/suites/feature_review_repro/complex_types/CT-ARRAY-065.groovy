suite("repro_ct_array_065") {
    boolean threw = false; String err = ""
    Object result = "UNKNOWN"
    try {
        def r = sql "SELECT array_with_constant(CAST(-1 AS BIGINT), 'x')"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-1 #2 INT_MIN overflow risk
    assertTrue(threw || result == null || result.toString() == "[]", "CT-ARRAY-065: BIGINT negative count safe (SEV-1 #2); threw=${threw} result=${result} err=${err}")
}
