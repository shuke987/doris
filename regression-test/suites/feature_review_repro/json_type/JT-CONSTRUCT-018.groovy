// JT-CONSTRUCT-018: object 0 参
suite("repro_jt_construct_018") {
    def r = sql """SELECT json_object()"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('{}'), "observed=${r}")
}
