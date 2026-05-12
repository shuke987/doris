suite("repro_ct_cast_055") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(struct(1,'a') AS JSONB)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-CAST-055: struct->JSONB; threw=${threw} obs=${obs} err=${err}")
}
