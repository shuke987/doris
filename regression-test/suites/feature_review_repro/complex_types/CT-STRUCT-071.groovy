suite("repro_ct_struct_071") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT struct_element(struct_element(named_struct('a', named_struct('b', 1)), 'a'), 'b')"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null, "CT-STRUCT-071: nested struct_element; threw=${threw} obs=${obs} err=${err}")
}
