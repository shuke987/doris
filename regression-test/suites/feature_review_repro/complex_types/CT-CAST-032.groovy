suite("repro_ct_cast_032") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(array(array(1)) AS ARRAY<INT>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-CAST-032: downgrade dim reject; threw=${threw} err=${err}")
}
