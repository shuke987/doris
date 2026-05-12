// JT-CONSTRUCT-029: object key 含 emoji
suite("repro_jt_construct_029") {
    def r = sql """SELECT json_object('🎉', 1)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('🎉'), "observed=${r}")
}
