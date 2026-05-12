suite("repro_ct_cast_056") {
    boolean threw = false; long sz = -1; String err = ""
    try {
        def r = sql "SELECT array_size(CAST(jsonb_parse('[1,2]') AS ARRAY<INT>))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 2L, "CT-CAST-056: jsonb->array; threw=${threw} sz=${sz} err=${err}")
}
