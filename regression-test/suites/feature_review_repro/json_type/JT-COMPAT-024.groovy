// JT-COMPAT-024: JSON_QUOTE MySQL
suite("repro_jt_compat_024") {
    def r = sql "SELECT JSON_QUOTE('hello')"
    assertEquals("\"hello\"", r[0][0].toString(), "JT-COMPAT-024; observed=${r}")
}
