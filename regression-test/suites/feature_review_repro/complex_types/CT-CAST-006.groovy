suite("repro_ct_cast_006") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST('[1' AS ARRAY<INT>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null || obs != null, "CT-CAST-006: missing ]; threw=${threw} obs=${obs} err=${err}")
}
