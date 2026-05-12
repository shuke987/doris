// JT-MODIFY-031: json_remove 不存在 key no-op
suite("repro_jt_modify_031") {
    def r = sql "SELECT json_remove(CAST('{\"a\":1}' AS JSONB), '\$.nope')"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1"), "JT-MODIFY-031; observed=${r}")
}
