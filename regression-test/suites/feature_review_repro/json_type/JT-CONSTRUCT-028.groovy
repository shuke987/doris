// JT-CONSTRUCT-028: object key 含中文
suite("repro_jt_construct_028") {
    def r = sql """SELECT json_object('中文', 1)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('中文'), "observed=${r}")
}
