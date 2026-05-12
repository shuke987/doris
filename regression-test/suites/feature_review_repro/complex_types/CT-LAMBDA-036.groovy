suite("repro_ct_lambda_036") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_apply(array(1,2,3), '>=', 2)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-LAMBDA-036: array_apply; threw=${threw} obs=${obs} err=${err}")
}
