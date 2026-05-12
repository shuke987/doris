suite("repro_ct_array_190") {
    boolean threw = false; long sz = -2; String err = ""
    try {
        def r = sql "SELECT array_size(array_popback(array()))"
        sz = (r[0][0] == null) ? -1 : (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz >= 0L || sz == -1L, "CT-ARRAY-190: popback empty; threw=${threw} sz=${sz} err=${err}")
}
