suite("repro_ct_struct_047") {
    boolean threw = false; String err = ""
    try { sql "SELECT named_struct('a',1,'a',2)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-047: dup name reject; threw=${threw} err=${err}")
}
