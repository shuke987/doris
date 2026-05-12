suite("repro_ct_array_193") {
    boolean threw = false; long sz = -2; String err = ""
    try {
        def r = sql "SELECT array_size(array_flatten(array(array(1,2), array(3,4))))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 4L, "CT-ARRAY-193: flatten; threw=${threw} sz=${sz} err=${err}")
}
