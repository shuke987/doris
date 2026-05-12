// JT-CAST-065: json_set value 参数 int → JSONB
suite("repro_jt_cast_065") {
    def r = sql "SELECT json_set(CAST('{\"a\":1}' AS JSONB), '\$.a', 9)"
    String v = r[0][0].toString()
    assertTrue(v.contains("\"a\":9"), "JT-CAST-065; observed=${r}")
}
