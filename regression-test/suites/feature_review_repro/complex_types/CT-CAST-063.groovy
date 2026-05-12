suite("repro_ct_cast_063") {
    boolean threw = false; String err = ""
    try { sql "SELECT CAST(123 AS ARRAY<INT>)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-CAST-063: INT->ARRAY reject; threw=${threw} err=${err}")
}
