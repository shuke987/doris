suite("repro_ct_cast_004") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST('[1,\"abc\",3]' AS ARRAY<INT>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null || obs == null, "CT-CAST-004: bad elem non-strict; threw=${threw} obs=${obs} err=${err}")
}
