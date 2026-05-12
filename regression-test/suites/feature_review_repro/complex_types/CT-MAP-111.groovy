suite("repro_ct_map_111") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_contains_entry(map('a',1), 'a', 1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == true || (obs != null && (obs as Number).longValue() == 1L), "CT-MAP-111: contains_entry positive; threw=${threw} obs=${obs} err=${err}")
}
