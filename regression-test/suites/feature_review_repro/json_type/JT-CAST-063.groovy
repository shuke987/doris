// JT-CAST-063: json_object NULL value
suite("repro_jt_cast_063") {
    def r = sql "SELECT json_object('k', NULL)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"k\":null"), "JT-CAST-063; observed=${r}")
}
