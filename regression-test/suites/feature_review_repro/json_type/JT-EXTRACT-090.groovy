// JT-EXTRACT-090: type int16 (small int)
suite("repro_jt_extract_090") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":1000}' AS JSONB), '\$.a')"
    String t = r[0][0].toString().toLowerCase()
    assertTrue(t == "int" || t.contains("int"), "JT-EXTRACT-090; observed=${r}")
}
