suite("repro_ct_array_196") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_flatten(array(1,2,3))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: 1D flatten -> reject or passthrough
    assertTrue(threw || obs != null, "CT-ARRAY-196: 1D flatten; threw=${threw} obs=${obs} err=${err}")
}
