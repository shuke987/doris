// JT-COMPAT-040: JSON_TYPE returns string type
suite("repro_jt_compat_040") {
    def r = sql "SELECT JSON_TYPE(CAST('[1,2,3]' AS JSONB), '\$')"
    String v = r[0][0].toString().toLowerCase()
    assertEquals("array", v, "JT-COMPAT-040; observed=${r}")
}
