// JT-MODIFY-020: json_replace 已有 key 改
suite("repro_jt_modify_020") {
    def r = sql "SELECT json_replace(CAST('{\"a\":1}' AS JSONB), '\$.a', 9)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":9"), "JT-MODIFY-020; observed=${r}")
}
