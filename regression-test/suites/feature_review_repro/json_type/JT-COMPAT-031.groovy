// JT-COMPAT-031: JSON_REPLACE MySQL
suite("repro_jt_compat_031") {
    def r = sql "SELECT JSON_REPLACE(CAST('{\"a\":1}' AS JSONB), '\$.a', 9)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":9"), "JT-COMPAT-031; observed=${r}")
}
