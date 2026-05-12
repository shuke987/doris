// JT-EXTRACT-087: jsonb_type null
suite("repro_jt_extract_087") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":null}' AS JSONB), '\$.a')"
    assertEquals("null", r[0][0].toString(), "JT-EXTRACT-087; observed=${r}")
}
