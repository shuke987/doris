// JT-COMPAT-033: JSON_EXTRACT 别名
suite("repro_jt_compat_033") {
    def r = sql "SELECT JSON_EXTRACT(CAST('[1,2,3]' AS JSONB), '\$[0]')"
    assertEquals("1", r[0][0].toString(), "JT-COMPAT-033; observed=${r}")
}
