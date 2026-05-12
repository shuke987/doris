suite("repro_ct_cast_054") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(map('a',1) AS JSONB)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-CAST-054: map->JSONB; threw=${threw} obs=${obs} err=${err}")
}
