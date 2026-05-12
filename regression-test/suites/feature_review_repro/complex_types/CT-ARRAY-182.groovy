suite("repro_ct_array_182") {
    boolean threw = false; Object result = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_concat(array(1,2), array('a','b'))"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || result != null, "CT-ARRAY-182: concat different elem types; threw=${threw} result=${result} err=${err}")
}
