// JT-EXTRACT-091: type int (any size)
suite("repro_jt_extract_091") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":42}' AS JSONB), '\$.a')"
    assertEquals("int", r[0][0].toString(), "JT-EXTRACT-091; observed=${r}")
}
