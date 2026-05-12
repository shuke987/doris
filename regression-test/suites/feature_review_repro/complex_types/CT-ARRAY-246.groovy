suite("repro_ct_array_246") {
    boolean threw = false; String err = ""
    try { sql "SELECT array_size(array_range(0, 1000001))" }
    catch (Exception e) { threw = true; err = e.toString() }
    // spec: InvalidArgument "exceeds the limit 1000000"
    assertTrue(threw, "CT-ARRAY-246: exceed 1M limit must reject; threw=${threw} err=${err}")
}
