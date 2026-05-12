// JT-CONSTRUCT-017: object 多对
suite("repro_jt_construct_017") {
    def r = sql """SELECT json_object('a',1,'b',2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a":1'), "JT-CONSTRUCT-017; observed=${r}")
}
