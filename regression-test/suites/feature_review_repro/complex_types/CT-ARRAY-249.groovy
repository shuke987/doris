suite("repro_ct_array_249") {
    boolean threw = false; long sz = -2; String err = ""
    try {
        def r = sql "SELECT array_size(array_with_constant(CAST(100 AS LARGEINT), 'x'))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 100L, "CT-ARRAY-249: LARGEINT count; threw=${threw} sz=${sz} err=${err}")
}
