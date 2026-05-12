suite("repro_ct_cast_021") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql """SELECT CAST('{"a":1' AS MAP<STRING,INT>)"""; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-CAST-021: missing }; threw=${threw} obs=${obs} err=${err}")
}
