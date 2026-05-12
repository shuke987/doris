suite("repro_ct_array_228") {
    boolean threw = false; String err = ""
    try {
        sql "SELECT array_distance(array(1.0,2.0), array(1.0,2.0), 'invalid_metric')"
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw, "CT-ARRAY-228: invalid metric reject; threw=${threw} err=${err}")
}
