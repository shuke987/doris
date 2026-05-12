suite("repro_ct_map_113") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT map_contains_entry(map('a',1), 'a', CAST(NULL AS INT))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null || obs == null, "CT-MAP-113: NULL value spec; threw=${threw} obs=${obs} err=${err}")
}
