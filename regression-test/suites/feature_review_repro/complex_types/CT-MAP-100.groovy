suite("repro_ct_map_100") {
    boolean threw = false; long innerSize = -1; String err = ""
    try {
        def r = sql "SELECT map_size(element_at(map('a', map('x',1,'x',2)), 'a'))"
        innerSize = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: inner map may or may not dedup; record
    assertTrue(threw || innerSize == 1L || innerSize == 2L, "CT-MAP-100: nested map dedup spec; threw=${threw} sz=${innerSize} err=${err}")
}
