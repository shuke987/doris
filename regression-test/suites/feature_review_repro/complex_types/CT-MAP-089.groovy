suite("repro_ct_map_089") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_entries(CAST(NULL AS MAP<STRING,INT>))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-MAP-089: NULL map_entries; threw=${threw} obs=${obs} err=${err}")
}
