suite("repro_ct_struct_058") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try { def r = sql "SELECT struct_element(struct(1, 'a'), 99)"; obs = r[0][0] } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-STRUCT-058: idx 99 out-of-range; threw=${threw} obs=${obs} err=${err}")
}
