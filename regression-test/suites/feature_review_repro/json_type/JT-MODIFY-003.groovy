// JT-MODIFY-003: json_set NULL value 存 jsonb null
suite("repro_jt_modify_003") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$.a', NULL)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":null"), "JT-MODIFY-003; observed=${r}")
}
