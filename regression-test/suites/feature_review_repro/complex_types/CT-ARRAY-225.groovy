suite("repro_ct_array_225") {
    boolean threw = false; String err = ""
    try {
        sql "SELECT array_distance(array(1.0,2.0), array(1.0,2.0,3.0), 'l2')"
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-ARRAY-225: unequal length reject; threw=${threw} err=${err}")
}
