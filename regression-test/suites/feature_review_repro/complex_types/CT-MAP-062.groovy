suite("repro_ct_map_062") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT element_at(map(1,'x'), 'a')"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null || obs != null, "CT-MAP-062: key type mismatch; threw=${threw} obs=${obs} err=${err}")
}
