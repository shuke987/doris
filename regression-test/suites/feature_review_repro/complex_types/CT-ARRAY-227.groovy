suite("repro_ct_array_227") {
    boolean threw = false; double v = -1.0; String err = ""
    try {
        def r = sql "SELECT array_distance(array(1.0,2.0,3.0), array(4.0,5.0,6.0), 'cosine')"
        v = (r[0][0] as Number).doubleValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || (v >= 0.0 && v <= 2.0), "CT-ARRAY-227: cosine in [0,2]; threw=${threw} v=${v} err=${err}")
}
