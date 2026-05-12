// JT-CONSTRUCT-035: object 嵌套 object value
suite("repro_jt_construct_035") {
    def r = sql """SELECT json_object('a', json_object('b',1))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"b":1'), "JT-CONSTRUCT-035; observed=${r}")
}
