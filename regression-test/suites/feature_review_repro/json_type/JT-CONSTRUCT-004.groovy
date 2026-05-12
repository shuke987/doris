// JT-CONSTRUCT-004: array NULL 元素 → jsonb null
suite("repro_jt_construct_004") {
    def r = sql """SELECT json_array(1, NULL, 3)"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('null'), "observed=${r}")
}
