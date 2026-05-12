// JT-COMPAT-021: JSON_ARRAY MySQL form
suite("repro_jt_compat_021") {
    def r = sql "SELECT JSON_ARRAY(1, 'a')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("\"a\""), "JT-COMPAT-021; observed=${r}")
}
