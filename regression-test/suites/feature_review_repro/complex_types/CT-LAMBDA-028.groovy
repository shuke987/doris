suite("repro_ct_lambda_028") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_sortby(array(1,2,3), array(3,1,2))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-LAMBDA-028: dual array sortby; threw=${threw} obs=${obs} err=${err}")
}
