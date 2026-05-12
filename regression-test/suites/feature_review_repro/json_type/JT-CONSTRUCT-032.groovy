// JT-CONSTRUCT-032: object value implicit cast 各类型
suite("repro_jt_construct_032") {
    def r = sql """SELECT json_object('b', true, 'd', 1.5)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('true'), "observed=${r}")
}
