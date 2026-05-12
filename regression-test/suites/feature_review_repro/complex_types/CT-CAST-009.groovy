suite("repro_ct_cast_009") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST('' AS ARRAY<INT>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-CAST-009: empty string; threw=${threw} obs=${obs} err=${err}")
}
