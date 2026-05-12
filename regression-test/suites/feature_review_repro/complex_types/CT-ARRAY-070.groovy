// CT-ARRAY-070: array_range(-1) behavior: [] / reject
suite("repro_ct_array_070") {
    boolean threw = false; String err = ""
    Object result = "UNKNOWN"; long sz = -2
    try {
        def r = sql "SELECT array_size(array_range(-1))"
        result = r[0][0]; sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 0, "CT-ARRAY-070: array_range(-1) should reject or return []; threw=${threw} sz=${sz} err=${err}")
}
