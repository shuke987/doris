suite("repro_ct_struct_057") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT struct_element(struct(1, 'a'), 0)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    // spec: 0 idx -> reject or NULL
    assertTrue(threw || obs == null, "CT-STRUCT-057: idx 0; threw=${threw} obs=${obs} err=${err}")
}
