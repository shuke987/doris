suite("repro_ct_struct_041") {
    boolean threw = false; String err = ""
    try { sql "SELECT struct()" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-041: 0-arg struct reject; threw=${threw} err=${err}")
}
