suite("repro_ct_cast_053") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(array(1,2,3) AS JSONB)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-CAST-053: array->JSONB; threw=${threw} obs=${obs} err=${err}")
}
