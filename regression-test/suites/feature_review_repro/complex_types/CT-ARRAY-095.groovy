suite("repro_ct_array_095") {
    boolean threw = false; Object result = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT element_at(array(1,2,3), '1')"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // either rejected or implicit cast to int idx
    assertTrue(threw || result != null, "CT-ARRAY-095: string idx reject or cast; threw=${threw} result=${result} err=${err}")
}
