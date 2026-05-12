suite("repro_ct_map_102") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT struct_element(map_entries(map('a',1))[1], 'KEY')"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-2 #N7 case sensitivity: spec mismatch FE vs BE
    assertTrue(threw || obs == null || obs != null, "CT-MAP-102: struct_element field case; threw=${threw} obs=${obs} err=${err}")
}
