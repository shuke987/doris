suite("repro_ct_cast_007") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST('1,2,3' AS ARRAY<INT>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-CAST-007: no brackets; threw=${threw} obs=${obs} err=${err}")
}
