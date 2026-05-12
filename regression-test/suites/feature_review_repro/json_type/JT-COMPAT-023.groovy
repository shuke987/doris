// JT-COMPAT-023: JSON_KEYS MySQL form
suite("repro_jt_compat_023") {
    def r = sql "SELECT JSON_KEYS(CAST('{\"a\":1,\"b\":2}' AS JSONB))"
    String v = r[0][0].toString()
    assertTrue(v.contains("a") && v.contains("b"), "JT-COMPAT-023; observed=${r}")
}
