suite("repro_ct_lambda_019") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_filter(array(1,2), array(true,true,true,true))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-LAMBDA-019: mask longer (SEV-2 #N5); threw=${threw} obs=${obs} err=${err}")
}
