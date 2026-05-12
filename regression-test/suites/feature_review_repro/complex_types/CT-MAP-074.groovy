suite("repro_ct_map_074") {
    def r = sql "SELECT map_keys(map('a',1,'b',2))"
    String s = r[0][0].toString()
    assertTrue(s.contains("a") && s.contains("b"), "CT-MAP-074: keys preserve; observed=${r}")
}
