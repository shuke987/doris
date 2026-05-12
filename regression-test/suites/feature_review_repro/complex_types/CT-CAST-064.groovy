suite("repro_ct_cast_064") {
    boolean threw = false; String err = ""
    try { sql "SELECT CAST(map('a',1) AS ARRAY<INT>)" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-CAST-064: MAP->ARRAY reject; threw=${threw} err=${err}")
}
