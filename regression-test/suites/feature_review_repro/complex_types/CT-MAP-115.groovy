suite("repro_ct_map_115") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_contains_entry(map(), 'a', 1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == false || (obs != null && (obs as Number).longValue() == 0L), "CT-MAP-115: empty map=false; threw=${threw} obs=${obs} err=${err}")
}
