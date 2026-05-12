// JT-CONSTRUCT-058: sort_json_object_keys 中文 / emoji 排序
suite("repro_jt_construct_058") {
    def r = sql """SELECT sort_json_object_keys(CAST('{"中文":1,"abc":2,"🎉":3}' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('abc'), "observed=${r}")
}
