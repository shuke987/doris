// JT-CONSTRUCT-016: object 1 对
suite("repro_jt_construct_016") {
    def r = sql """SELECT json_object('k',1)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"k":1'), "JT-CONSTRUCT-016; observed=${r}")
}
