suite("repro_ct_map_106") {
    boolean threw = false; long sz = -2; String err = ""
    try { def r = sql "SELECT map_size(str_to_map('a：1，b：2', '，', '：'))"; sz = (r[0][0] as Number).longValue() } catch (Exception e) { threw = true; err = e.toString() }
    assertTrue(threw || sz == 2L, "CT-MAP-106: chinese delim; threw=${threw} sz=${sz} err=${err}")
}
