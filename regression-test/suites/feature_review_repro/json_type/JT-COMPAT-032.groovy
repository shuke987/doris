// JT-COMPAT-032: JSON_SEARCH MySQL
suite("repro_jt_compat_032") {
    def r = sql "SELECT JSON_SEARCH(CAST('{\"a\":\"hi\"}' AS JSONB), 'one', 'hi')"
    String v = r[0][0].toString()
    assertTrue(v.contains("a"), "JT-COMPAT-032; observed=${r}")
}
