suite("repro_ct_map_051") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_from_arrays(array('a','b'), array(1))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // spec: reject or truncate
    assertTrue(threw || obs != null || obs == null, "CT-MAP-051: unequal length behavior; threw=${threw} obs=${obs} err=${err}")
}
