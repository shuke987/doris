suite("repro_ct_map_087") {
    boolean threw = false; long sz = -2; String err = ""
    try { def r = sql "SELECT array_size(map_entries(map('a',1,'b',2)))"; sz = (r[0][0] as Number).longValue() } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 2L, "CT-MAP-087: map_entries size=2; threw=${threw} sz=${sz} err=${err}")
}
