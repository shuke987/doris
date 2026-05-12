// JT-CONSTRUCT-022: object NULL value
suite("repro_jt_construct_022") {
    def r = sql """SELECT json_object('k', NULL)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('null'), "observed=${r}")
}
