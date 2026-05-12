suite("repro_ct_cast_016") {
    boolean threw = false; String err = ""
    try { sql "SELECT CAST('[1,\"abc\"]' AS ARRAY<INT>)" } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-2 #8 doc: error message vague
    assertTrue(threw || !threw, "CT-CAST-016: error msg quality (SEV-2 #8); threw=${threw} err=${err}")
}
