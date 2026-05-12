// JT-EXTRACT-088: type bool
suite("repro_jt_extract_088") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":true}' AS JSONB), '\$.a')"
    assertEquals("bool", r[0][0].toString(), "JT-EXTRACT-088; observed=${r}")
}
