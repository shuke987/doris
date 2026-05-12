// JT-COMPAT-027: JSON_SET MySQL
suite("repro_jt_compat_027") {
    def r = sql "SELECT JSON_SET(CAST('{\"a\":1}' AS JSONB), '\$.b', 2)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"b\":2"), "JT-COMPAT-027; observed=${r}")
}
