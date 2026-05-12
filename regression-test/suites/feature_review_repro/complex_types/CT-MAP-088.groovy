suite("repro_ct_map_088") {
    boolean threw = false; long sz = -2; String err = ""
    try { def r = sql "SELECT array_size(map_entries(map()))"; sz = (r[0][0] as Number).longValue() } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 0L, "CT-MAP-088: empty map_entries; threw=${threw} sz=${sz} err=${err}")
}
