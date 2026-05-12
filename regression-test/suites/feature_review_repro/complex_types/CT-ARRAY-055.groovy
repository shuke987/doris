// CT-ARRAY-055: array(1, 'a') mixed type
suite("repro_ct_array_055") {
    boolean threw = false; String err = ""
    long size = -1
    try {
        def r = sql "SELECT array_size(array(1, 'a'))"
        size = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: either rejected or promoted to ARRAY<STRING> with size=2
    assertTrue(threw || size == 2L, "CT-ARRAY-055: mixed types should promote or reject; threw=${threw} size=${size} err=${err}")
}
