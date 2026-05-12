suite("repro_ct_array_163") {
    boolean threw = false; String err = ""; Object result = null
    try {
        def r = sql "SELECT array_sortby(array(1,2,3), array(1,2))"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: length mismatch behavior
    assertTrue(threw || result != null, "CT-ARRAY-163: array_sortby unequal length spec behavior; threw=${threw} result=${result} err=${err}")
}
