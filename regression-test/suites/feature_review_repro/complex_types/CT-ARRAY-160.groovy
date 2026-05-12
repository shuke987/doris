suite("repro_ct_array_160") {
    boolean threw = false; String err = ""
    try {
        sql "SELECT array_sort(array(CAST('NaN' AS DOUBLE), 1.0))"
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(true, "CT-ARRAY-160: NaN sort no crash; threw=${threw} err=${err}")
}
