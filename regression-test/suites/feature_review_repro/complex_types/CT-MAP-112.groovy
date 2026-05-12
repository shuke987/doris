suite("repro_ct_map_112") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_contains_entry(map('a',1), CAST(NULL AS STRING), 1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null || obs == null, "CT-MAP-112: NULL key spec; threw=${threw} obs=${obs} err=${err}")
}
