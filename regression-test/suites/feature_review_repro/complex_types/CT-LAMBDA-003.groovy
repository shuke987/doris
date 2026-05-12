suite("repro_ct_lambda_003") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT array_map((x,y)->x+y, array(1,2), array(10))"; obs = r[0][0] }
    catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-LAMBDA-003: unequal length reject; threw=${threw} err=${err}")
}
