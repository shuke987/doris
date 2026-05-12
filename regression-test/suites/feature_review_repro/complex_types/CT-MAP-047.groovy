suite("repro_ct_map_047") {
    boolean threw = false; String err = ""
    try { sql "SELECT map('a',1,'b')" } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-MAP-047: odd args reject; threw=${threw} err=${err}")
}
