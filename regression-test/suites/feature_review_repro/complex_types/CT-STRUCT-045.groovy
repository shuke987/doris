suite("repro_ct_struct_045") {
    boolean threw = false; String err = ""
    try { sql "SELECT named_struct()" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-045: 0-arg named_struct reject; threw=${threw} err=${err}")
}
