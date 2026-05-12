suite("repro_ct_cast_065") {
    boolean threw = false; String err = ""
    try { sql "SELECT CAST(struct(1,'a') AS ARRAY<INT>)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-CAST-065: STRUCT->ARRAY reject; threw=${threw} err=${err}")
}
