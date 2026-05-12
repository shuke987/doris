suite("repro_ct_array_172") {
    boolean threw = false; Object result = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_slice(array(1,2,3), 100, 5)"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || result != null, "CT-ARRAY-172: out-of-range offset; threw=${threw} result=${result} err=${err}")
}
