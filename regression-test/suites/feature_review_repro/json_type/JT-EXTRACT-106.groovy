// JT-EXTRACT-106: jsonb_keys 对非 object
suite("repro_jt_extract_106") {
    def r = sql "SELECT jsonb_keys(CAST('[1,2]' AS JSONB))"
    assertEquals(null, r[0][0], "JT-EXTRACT-106: non-object → NULL; observed=${r}")
}
