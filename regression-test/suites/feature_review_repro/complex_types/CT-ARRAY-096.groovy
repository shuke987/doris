suite("repro_ct_array_096") {
    boolean threw = false; Object result = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT element_at(array(1,2,3), 1.5)"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || result != null, "CT-ARRAY-096: float idx reject or truncate; threw=${threw} result=${result} err=${err}")
}
