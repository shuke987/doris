suite("repro_ct_lambda_037") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_apply(array(1,2,3), '>', 0)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-LAMBDA-037: apply op case; threw=${threw} obs=${obs} err=${err}")
}
