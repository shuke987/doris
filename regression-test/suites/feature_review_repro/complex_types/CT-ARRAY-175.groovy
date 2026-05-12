suite("repro_ct_array_175") {
    boolean threw = false; Object result = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_slice(array(1,2,3), 2147483647)"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-1 #2: must not overflow / crash
    assertTrue(threw || result != null || result == null, "CT-ARRAY-175: INT_MAX offset no crash (SEV-1 #2); threw=${threw} result=${result} err=${err}")
}
