suite("repro_ct_lambda_038") {
    boolean threw = false; String err = ""
    try { sql "SELECT array_apply(array(1,2,3), '!!!', 0)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-LAMBDA-038: invalid op reject; threw=${threw} err=${err}")
}
