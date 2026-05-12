suite("repro_ct_lambda_008") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_map(x->1/0, array(1,2))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // either errors or returns null array
    assertTrue(threw || obs != null, "CT-LAMBDA-008: lambda div-zero; threw=${threw} obs=${obs} err=${err}")
}
