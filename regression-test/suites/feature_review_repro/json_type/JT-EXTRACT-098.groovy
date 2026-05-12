// JT-EXTRACT-098: type object
suite("repro_jt_extract_098") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":{}}' AS JSONB), '\$.a')"
    assertEquals("object", r[0][0].toString(), "JT-EXTRACT-098; observed=${r}")
}
