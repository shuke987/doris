// JT-MODIFY-045: json_set 嵌入 JSONB value
suite("repro_jt_modify_045") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$.a', CAST('{\"x\":1}' AS JSONB))"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"x\":1"), "JT-MODIFY-045; observed=${r}")
}
