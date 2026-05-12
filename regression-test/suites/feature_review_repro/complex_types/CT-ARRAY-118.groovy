suite("repro_ct_array_118") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_contains(array(1,2,3), '1')"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // implicit cast or reject
    assertTrue(threw || obs != null, "CT-ARRAY-118: type mismatch reject or cast; threw=${threw} obs=${obs} err=${err}")
}
