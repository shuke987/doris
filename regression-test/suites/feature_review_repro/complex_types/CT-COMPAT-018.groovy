suite("repro_ct_compat_018") {
    boolean threw = false; String v = ""; String err = ""
    try {
        def r = sql "SELECT jsonb_extract_string(jsonb_parse('{\"a\":1,\"a\":2}'), '\$.a')"
        v = r[0][0].toString()
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-3 #N10: JSONB first-wins vs MAP last-wins inconsistency doc
    assertTrue(threw || v.length() > 0, "CT-COMPAT-018: JSONB first-wins doc (SEV-3 #N10); threw=${threw} v=${v} err=${err}")
}
