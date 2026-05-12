// JT-COMPAT-020: JSON_OBJECT (MySQL aliased)
suite("repro_jt_compat_020") {
    def r = sql "SELECT JSON_OBJECT('a',1,'b',2)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":1") && v.contains("\"b\":2"),
        "JT-COMPAT-020; observed=${r}")
}
