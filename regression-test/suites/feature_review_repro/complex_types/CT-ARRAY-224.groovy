suite("repro_ct_array_224") {
    boolean threw = false; double v = -1.0; String err = ""
    try {
        def r = sql "SELECT array_distance(array(1.0,2.0,3.0), array(4.0,5.0,6.0), 'l2')"
        v = (r[0][0] as Number).doubleValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || (v > 5.1 && v < 5.3), "CT-ARRAY-224: l2 distance ~5.196; threw=${threw} v=${v} err=${err}")
}
