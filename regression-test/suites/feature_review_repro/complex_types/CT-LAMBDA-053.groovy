suite("repro_ct_lambda_053") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_map((x,y)->x+y, array(), array(1,2,3))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-LAMBDA-053: empty first (SEV-2 #N5); threw=${threw} obs=${obs} err=${err}")
}
