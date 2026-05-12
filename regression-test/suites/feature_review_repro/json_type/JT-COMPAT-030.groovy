// JT-COMPAT-030: JSON_INSERT MySQL
suite("repro_jt_compat_030") {
    def r = sql "SELECT JSON_INSERT(CAST('{\"a\":1}' AS JSONB), '\$.b', 2)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"b\":2"), "JT-COMPAT-030; observed=${r}")
}
