suite("repro_ct_cast_020") {
    def r = sql """SELECT map_size(CAST('{"a":1,"a":2}' AS MAP<STRING,INT>)), element_at(CAST('{"a":1,"a":2}' AS MAP<STRING,INT>), 'a')"""
    long sz = (r[0][0] as Number).longValue()
    long v = (r[0][1] as Number).longValue()
    // SEV-3 #N10 last-wins
    assertEquals(1L, sz, "CT-CAST-020: dup dedup (SEV-3 #N10); observed=${r}")
    assertEquals(2L, v, "CT-CAST-020: last-wins=2; observed=${r}")
}
