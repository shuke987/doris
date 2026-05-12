// JT-EXTRACT-099: type array
suite("repro_jt_extract_099") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":[]}' AS JSONB), '\$.a')"
    assertEquals("array", r[0][0].toString(), "JT-EXTRACT-099; observed=${r}")
}
