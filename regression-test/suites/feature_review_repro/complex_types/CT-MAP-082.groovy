suite("repro_ct_map_082") {
    def r = sql "SELECT map_keys(map('a',1,'b',2)), map_values(map('a',1,'b',2))"
    String k = r[0][0].toString(); String v = r[0][1].toString()
    assertTrue(k.contains("a") && k.contains("b") && v.contains("1") && v.contains("2"), "CT-MAP-082: keys/values aligned; observed=${r}")
}
