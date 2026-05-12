// JT-EXTRACT-105: jsonb_keys 嵌套 path
suite("repro_jt_extract_105") {
    def r = sql "SELECT jsonb_keys(CAST('{\"a\":{\"x\":1,\"y\":2}}' AS JSONB), '\$.a')"
    String v = r[0][0].toString()
    assertTrue(v.contains("x") && v.contains("y"), "JT-EXTRACT-105; observed=${r}")
}
