// JT-CONSTRUCT-011: json_object NULL value
suite("repro_jt_construct_011") {
    def r = sql "SELECT json_object('k', NULL)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"k\":null"), "JT-CONSTRUCT-011; observed=${r}")
}
