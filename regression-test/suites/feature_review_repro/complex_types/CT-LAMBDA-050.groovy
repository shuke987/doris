suite("repro_ct_lambda_050") {
    boolean threw = false; String err = ""
    try { sql "SELECT array_map((x,y,z)->x+y+z, array(1,2),array(3,4),array(5))" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-LAMBDA-050: 3rd unequal reject; threw=${threw} err=${err}")
}
