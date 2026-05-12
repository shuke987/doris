// JT-COMPAT-010: jsonb_extract 函数大小写不敏感
suite("repro_jt_compat_010") {
    def r1 = sql "SELECT JSONB_EXTRACT(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r1[0][0].toString(), "JT-COMPAT-010; observed=${r1}")
}
