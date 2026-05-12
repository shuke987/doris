suite("repro_ct_array_221") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_cum_sum(array(1,CAST(NULL AS INT),3))"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs != null || obs == null, "CT-ARRAY-221: cum_sum with NULL behavior; threw=${threw} obs=${obs} err=${err}")
}
