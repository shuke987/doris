// JT-CONSTRUCT-036: object 嵌套 array value
suite("repro_jt_construct_036") {
    def r = sql """SELECT json_object('a', json_array(1,2))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('[1,2]'), "observed=${r}")
}
