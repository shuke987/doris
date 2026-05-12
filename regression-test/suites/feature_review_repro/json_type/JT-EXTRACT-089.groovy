// JT-EXTRACT-089: jsonb_type int8
suite("repro_jt_extract_089") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":100}' AS JSONB), '\$.a')"
    assertEquals("int", r[0][0].toString(), "JT-EXTRACT-089; observed=${r}")
}
