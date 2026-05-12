// JT-CONSTRUCT-007: array 嵌套 object
suite("repro_jt_construct_007") {
    def r = sql """SELECT json_array(json_object('a',1))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('{"a":1}'), "JT-CONSTRUCT-007; observed=${r}")
}
