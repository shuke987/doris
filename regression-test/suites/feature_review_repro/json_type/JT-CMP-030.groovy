// JT-CMP-030: sort_json_object_keys dedup 保留首个
suite("repro_jt_cmp_030") {
    def r = sql """SELECT sort_json_object_keys(CAST('{"b":2,"a":1,"b":3}' AS JSONB))"""
    String v = r[0][0] == null ? "null" : r[0][0].toString()
    assertTrue(v.contains('"a"'), "JT-CMP-030; observed=${r}")
}
