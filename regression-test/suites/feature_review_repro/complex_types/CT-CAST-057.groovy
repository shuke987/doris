suite("repro_ct_cast_057") {
    boolean threw = false; long sz = -1; String err = ""
    try {
        def r = sql "SELECT map_size(CAST(jsonb_parse('{}') AS MAP<STRING,INT>))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 0L, "CT-CAST-057: jsonb {} -> map; threw=${threw} sz=${sz} err=${err}")
}
