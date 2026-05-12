suite("repro_ct_array_247") {
    boolean threw = false; Object obs = "UNKNOWN"; String err = ""
    try {
        def r = sql "SELECT array_range(CAST('invalid' AS DATETIME), CAST('2025-01-01' AS DATETIME), 1, 'day')"
        obs = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || obs == null, "CT-ARRAY-247: invalid date -> NULL or reject; threw=${threw} obs=${obs} err=${err}")
}
