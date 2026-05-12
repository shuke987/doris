// JT-COMPAT-025: JSON_UNQUOTE MySQL
suite("repro_jt_compat_025") {
    def r = sql "SELECT JSON_UNQUOTE('\"hello\"')"
    assertEquals("hello", r[0][0].toString(), "JT-COMPAT-025; observed=${r}")
}
