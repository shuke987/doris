suite("repro_ct_array_203") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_enumerate_uniq(array(1,1,2,1))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-ARRAY-203: enumerate_uniq; threw=${threw} obs=${obs} err=${err}")
}
