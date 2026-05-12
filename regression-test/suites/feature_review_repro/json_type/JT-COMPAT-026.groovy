// JT-COMPAT-026: JSON_CONTAINS MySQL form
suite("repro_jt_compat_026") {
    def r = sql "SELECT JSON_CONTAINS(CAST('[1,2,3]' AS JSONB), CAST('2' AS JSONB))"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-COMPAT-026; observed=${r}")
}
