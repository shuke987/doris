suite("repro_ct_map_052") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_from_arrays(array(CAST(NULL AS STRING),'a'), array(1,2))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-MAP-052: NULL key in array; threw=${threw} obs=${obs} err=${err}")
}
