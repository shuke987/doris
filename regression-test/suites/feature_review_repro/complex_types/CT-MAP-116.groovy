suite("repro_ct_map_116") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_contains_entry(CAST(NULL AS MAP<STRING,INT>), 'a', 1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-MAP-116: NULL map=NULL; threw=${threw} obs=${obs} err=${err}")
}
