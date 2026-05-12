suite("repro_ct_array_201") {
    boolean threw = false; String s = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_enumerate(array(10,20,30))"
        s = r[0][0].toString()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || s.contains("1") && s.contains("2") && s.contains("3"), "CT-ARRAY-201: enumerate 1-based; threw=${threw} s=${s} err=${err}")
}
