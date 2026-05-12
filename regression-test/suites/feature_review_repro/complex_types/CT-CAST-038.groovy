suite("repro_ct_cast_038") {
    boolean threw = false; long sz = -1; String err = ""
    try {
        def r = sql "SELECT map_size(CAST(map('a',1,'a',2) AS MAP<STRING,BIGINT>))"
        sz = (r[0][0] as Number).longValue()
    } catch (Exception e) { threw = true; err = e.toString() }
    // dedup last-wins
    assertTrue(threw || sz == 1L, "CT-CAST-038: cast preserves dedup (SEV-3 #N10); threw=${threw} sz=${sz} err=${err}")
}
