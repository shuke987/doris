// JT-EXTRACT-041: extract_string 对 object → NULL
suite("repro_jt_extract_041") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":{\"x\":1}}' AS JSONB), '\$.a')"
    assertEquals(null, r[0][0],
        "JT-EXTRACT-041; observed=${r}")
}
