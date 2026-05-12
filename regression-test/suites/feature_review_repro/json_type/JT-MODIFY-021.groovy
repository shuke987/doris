// JT-MODIFY-021: json_replace 不存在 key no-op
suite("repro_jt_modify_021") {
    def r = sql "SELECT json_replace(CAST('{\"a\":1}' AS JSONB), '\$.b', 9)"
    String v = r[0][0].toString()
    assertTrue(!v.contains("\"b\":") && v.contains("\"a\":1"),
        "JT-MODIFY-021: replace non-existent key no-op; observed=${r}")
}
