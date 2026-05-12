suite("repro_ct_cast_031") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(array(1,2) AS ARRAY<ARRAY<INT>>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-CAST-031: cross-dim cast reject; threw=${threw} err=${err}")
}
