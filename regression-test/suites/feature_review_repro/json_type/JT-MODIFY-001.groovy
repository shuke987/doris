// JT-MODIFY-001: json_set 添加新 key
suite("repro_jt_modify_001") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$.b', 2)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"b\":2"), "JT-MODIFY-001; observed=${r}")
}
