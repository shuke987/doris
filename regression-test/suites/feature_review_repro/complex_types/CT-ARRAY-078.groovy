// CT-ARRAY-078: array_repeat('a', -1) reject / []
suite("repro_ct_array_078") {
    boolean threw = false; long sz = -2; String err = ""
    try {
        def r = sql "SELECT array_size(array_repeat('a', -1))"
        sz = (r[0][0] == null) ? -1 : (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 0 || sz == -1, "CT-ARRAY-078: negative count reject/[]/NULL; threw=${threw} sz=${sz} err=${err}")
}
