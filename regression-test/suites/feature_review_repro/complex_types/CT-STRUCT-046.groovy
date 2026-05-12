suite("repro_ct_struct_046") {
    boolean threw = false; String err = ""
    try { sql "SELECT named_struct('a',1,'b')" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-STRUCT-046: odd args reject; threw=${threw} err=${err}")
}
