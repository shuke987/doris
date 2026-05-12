suite("repro_ct_lambda_041") {
    boolean threw = false; String err = ""
    try { sql "SELECT array_map(x->y, array(1,2))" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-LAMBDA-041: undefined var reject; threw=${threw} err=${err}")
}
