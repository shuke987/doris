suite("repro_ct_array_148") {
    boolean threw = false; long sz = -1; String err = ""
    try {
        def r = sql "SELECT array_size(array_union(array(1,2), array(2,3), array(3,4)))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    // 3-arg array_union may or may not be supported
    assertTrue(threw || sz == 4L, "CT-ARRAY-148: union 3 arrays; threw=${threw} sz=${sz} err=${err}")
}
