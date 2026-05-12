// JT-EXTRACT-043: extract_string 对 jsonb null
suite("repro_jt_extract_043") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":null}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0], "JT-EXTRACT-043; observed=${r}")
}
