// JT-EXTRACT-095: type double
suite("repro_jt_extract_095") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":3.14}' AS JSONB), '\$.a')"
    assertEquals("double", r[0][0].toString(), "JT-EXTRACT-095; observed=${r}")
}
