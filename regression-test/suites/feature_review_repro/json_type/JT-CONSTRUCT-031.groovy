// JT-CONSTRUCT-031: object value implicit cast int
suite("repro_jt_construct_031") {
    def r = sql """SELECT json_object('k', 1)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"k":1'), "JT-CONSTRUCT-031; observed=${r}")
}
