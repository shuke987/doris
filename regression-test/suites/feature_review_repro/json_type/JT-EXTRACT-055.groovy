// JT-EXTRACT-055: extract_int 对 object
suite("repro_jt_extract_055") {
    def r = sql "SELECT jsonb_extract_int(CAST('{\"a\":{\"x\":1}}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-055; observed=${r}")
}
