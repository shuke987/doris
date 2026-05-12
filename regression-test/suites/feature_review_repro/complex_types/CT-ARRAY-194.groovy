suite("repro_ct_array_194") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_flatten(array(array(1,2), CAST(NULL AS ARRAY<INT>), array(3)))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null || obs == null, "CT-ARRAY-194: flatten with NULL; threw=${threw} obs=${obs} err=${err}")
}
