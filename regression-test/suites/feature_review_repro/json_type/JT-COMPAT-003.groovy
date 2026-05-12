// JT-COMPAT-003: MySQL JSON_LENGTH 别名
suite("repro_jt_compat_003") {
    def r = sql "SELECT JSON_LENGTH(CAST('[1,2,3]' AS JSONB))"
    assertEquals("3", r[0][0].toString(), "JT-COMPAT-003; observed=${r}")
}
