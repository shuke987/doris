suite("repro_ct_struct_050") {
    boolean threw = false; String err = ""
    try { sql "SELECT named_struct(NULL, 1)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-050: NULL field name reject; threw=${threw} err=${err}")
}
