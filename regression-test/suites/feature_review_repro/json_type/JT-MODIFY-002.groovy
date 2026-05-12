// JT-MODIFY-002: json_set 修改已有 key
suite("repro_jt_modify_002") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$.a', 9)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":9"), "JT-MODIFY-002; observed=${r}")
}
