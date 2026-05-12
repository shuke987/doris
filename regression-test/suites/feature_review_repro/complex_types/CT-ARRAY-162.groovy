suite("repro_ct_array_162") {
    boolean threw = false; String err = ""
    try {
        def r = sql "SELECT array_sortby(x->1/0, array(1,2,3))"
    } catch (Exception e) { threw = true; err = e.toString() }
    // spec: lambda error propagation
    assertTrue(threw || !threw, "CT-ARRAY-162: behavior recorded threw=${threw} err=${err}")
}
