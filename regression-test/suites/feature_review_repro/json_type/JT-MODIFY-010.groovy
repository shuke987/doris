// JT-MODIFY-010: json_insert 已有 key no-op
suite("repro_jt_modify_010") {
    def r = sql "SELECT json_insert(CAST('{\"a\":1}' AS JSONB), '\$.a', 9)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"),
        "JT-MODIFY-010: insert existing key should no-op; observed=${r}")
}
