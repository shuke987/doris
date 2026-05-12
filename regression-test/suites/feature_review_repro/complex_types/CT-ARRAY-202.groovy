suite("repro_ct_array_202") {
    boolean threw = false; long sz = -2; String err = ""
    try {
        def r = sql "SELECT array_size(array_enumerate(array()))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 0L, "CT-ARRAY-202: enumerate empty; threw=${threw} sz=${sz} err=${err}")
}
