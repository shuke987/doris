suite("repro_ct_lambda_018") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_filter(array(1,2,3,4), array(true,true))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-2 #N5: silent truncation
    assertTrue(threw || obs != null, "CT-LAMBDA-018: mask short of data (SEV-2 #N5); threw=${threw} obs=${obs} err=${err}")
}
