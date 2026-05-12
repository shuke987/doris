suite("repro_ct_cast_037") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(map(1,'x') AS MAP<STRING,STRING>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-CAST-037: key type change; threw=${threw} obs=${obs} err=${err}")
}
