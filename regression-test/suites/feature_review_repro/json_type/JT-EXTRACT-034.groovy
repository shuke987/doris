// JT-EXTRACT-034: scalar-scalar extract
suite("repro_jt_extract_034") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-034; observed=${r}")
}
