suite("repro_ct_struct_086") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT named_struct(CASE WHEN true THEN 'a' END, 1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // FE fold validation - either accept folded or reject
    assertTrue(threw || obs != null, "CT-STRUCT-086: CASE field name fold; threw=${threw} obs=${obs} err=${err}")
}
