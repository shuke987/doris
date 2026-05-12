suite("repro_ct_struct_048") {
    boolean threw = false; String err = ""
    try { sql "SELECT named_struct(CAST('a' AS STRING) + '', 1)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-048: non-literal field name reject; threw=${threw} err=${err}")
}
