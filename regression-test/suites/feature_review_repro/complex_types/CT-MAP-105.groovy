suite("repro_ct_map_105") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT str_to_map('a:1', '', ':')"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-MAP-105: empty delim fallback; threw=${threw} obs=${obs} err=${err}")
}
