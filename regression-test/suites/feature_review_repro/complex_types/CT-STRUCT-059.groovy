suite("repro_ct_struct_059") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT struct_element(struct(1, 'a'), -1)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-STRUCT-059: idx -1 reject; threw=${threw} obs=${obs} err=${err}")
}
