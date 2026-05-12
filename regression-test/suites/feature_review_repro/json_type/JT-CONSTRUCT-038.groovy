// JT-CONSTRUCT-038: object 输出 key 顺序
suite("repro_jt_construct_038") {
    def r = sql """SELECT json_object('z',1,'a',2)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a"'), "JT-CONSTRUCT-038; observed=${r}")
}
