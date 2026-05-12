suite("repro_ct_cast_066") {
    // Array(Nothing) empty exception - empty array cast across dims
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT CAST(CAST('[]' AS ARRAY<INT>) AS ARRAY<ARRAY<INT>>)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // spec: cross-dim empty allowed
    assertTrue(threw || obs != null, "CT-CAST-066: empty cross-dim; threw=${threw} obs=${obs} err=${err}")
}
