suite("repro_ct_lambda_052") {
    boolean threw = false; String err = ""
    try { sql "SELECT array_map((x,y)->x+y, array(1), array(1,2))" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-LAMBDA-052: short+long reject; threw=${threw} err=${err}")
}
