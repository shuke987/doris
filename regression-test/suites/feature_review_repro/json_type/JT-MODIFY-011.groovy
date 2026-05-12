// JT-MODIFY-011: json_insert 新 key add
suite("repro_jt_modify_011") {
    def r = sql "SELECT json_insert(CAST('{\"a\":1}' AS JSONB), '\$.b', 2)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"b\":2"), "JT-MODIFY-011; observed=${r}")
}
