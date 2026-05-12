// CT-ARRAY-071: array_range(1, 5, 0) step=0 reject (NEW-SEV-N12)
suite("repro_ct_array_071") {
    boolean threw = false; String err = ""
    Object result = null
    try {
        def r = sql "SELECT array_range(1, 5, 0)"
        result = r[0][0]
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: should reject (avoid infinite loop). NEW-SEV-N12: current returns NULL silently
    assertTrue(threw, "CT-ARRAY-071: array_range step=0 should reject (NEW-SEV-N12 currently silent NULL); threw=${threw} result=${result} err=${err}")
}
