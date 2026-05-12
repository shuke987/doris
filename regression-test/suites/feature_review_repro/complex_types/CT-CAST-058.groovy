suite("repro_ct_cast_058") {
    boolean threw = false; long sz = -1; String err = ""
    try {
        def r = sql "SELECT map_size(CAST(jsonb_parse('{\"a\":1,\"a\":2}') AS MAP<STRING,INT>))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    // SEV-3 #N10: MAP last-wins vs JSONB first-wins
    assertTrue(threw || sz == 1L, "CT-CAST-058: dup key cast (SEV-3 #N10); threw=${threw} sz=${sz} err=${err}")
}
