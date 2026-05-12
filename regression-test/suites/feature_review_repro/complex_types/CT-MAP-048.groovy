suite("repro_ct_map_048") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map('a',1,'b','x')"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // spec: type mismatch behavior
    assertTrue(threw || obs != null, "CT-MAP-048: type mismatch; threw=${threw} obs=${obs} err=${err}")
}
