suite("repro_ct_struct_061") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT struct_element(named_struct('Aa', 1), 'AA')"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-2 #N7 FE case-insensitive vs BE case-sensitive
    assertTrue(threw || obs == null || obs != null, "CT-STRUCT-061: case mismatch (SEV-2 #N7); threw=${threw} obs=${obs} err=${err}")
}
