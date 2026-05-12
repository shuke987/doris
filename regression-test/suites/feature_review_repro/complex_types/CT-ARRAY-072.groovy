// CT-ARRAY-072: array_range(5, 1, -1) reverse (NEW-SEV-N12)
suite("repro_ct_array_072") {
    boolean threw = false
    long sz = -2
    Object result = "UNKNOWN"
    try {
        def r = sql "SELECT array_range(5, 1, -1)"
        result = r[0][0]
        def r2 = sql "SELECT array_size(array_range(5, 1, -1))"
        sz = (r2[0][0] == null) ? -1 : (r2[0][0] as Number).longValue()
    } catch (Exception e) { threw = true }
    // spec: reverse range, expect [5,4,3,2] size=4. NEW-SEV-N12: currently returns NULL
    assertEquals(4L, sz, "CT-ARRAY-072: array_range(5,1,-1) should be [5,4,3,2] size=4 (NEW-SEV-N12 currently NULL); result=${result}")
}
