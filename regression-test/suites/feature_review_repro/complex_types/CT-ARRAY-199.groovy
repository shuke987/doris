suite("repro_ct_array_199") {
    boolean threw = false; long sz = -2; String err = ""
    try {
        def r = sql "SELECT array_size(array_zip(array(1,2), array('a','b')))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 2L, "CT-ARRAY-199: array_zip; threw=${threw} sz=${sz} err=${err}")
}
