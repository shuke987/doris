suite("repro_ct_struct_081") {
    boolean threw = false; String err = ""
    try { sql "SELECT struct_element(struct(1,'a'), CAST(9999999999 AS BIGINT))" } catch (Exception e) { threw = true; err = e.toString() }
    // spec: error msg with specific number
    assertTrue(threw, "CT-STRUCT-081: BIGINT extreme reject with msg; threw=${threw} err=${err}")
}
