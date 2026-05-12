// JT-EXTRACT-070: extract_bool 对 null
suite("repro_jt_extract_070") {
    def r = sql "SELECT jsonb_extract_bool(CAST('{\"a\":null}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-070; observed=${r}")
}
