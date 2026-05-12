suite("repro_ct_struct_063") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT struct_element(struct(1,'a'), CAST(NULL AS STRING))"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-STRUCT-063: NULL idx; threw=${threw} obs=${obs} err=${err}")
}
