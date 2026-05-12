// JT-CONSTRUCT-006: array 嵌套 array
suite("repro_jt_construct_006") {
    def r = sql """SELECT json_array(json_array(1,2))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('[[1,2]]'), "observed=${r}")
}
