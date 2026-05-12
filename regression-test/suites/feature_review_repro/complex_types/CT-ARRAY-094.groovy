suite("repro_ct_array_094") {
    boolean threw = false; String err = ""
    Object result = "UNKNOWN"
    try {
        def r = sql "SELECT element_at(array(1,2,3), CAST('170141183460469231731687303715884105727' AS LARGEINT))"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: should return NULL; not crash
    assertTrue(threw || result == null, "CT-ARRAY-094: LARGEINT extreme idx no crash (SEV-1 #2); threw=${threw} result=${result} err=${err}")
}
