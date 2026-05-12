// JT-EXTRACT-038: extract_string 正向
suite("repro_jt_extract_038") {
    def r = sql "SELECT jsonb_extract_string(CAST('{\"a\":\"hi\"}' AS JSONB), '\$.a')"
    assertEquals("hi", r[0][0].toString(), "JT-EXTRACT-038; observed=${r}")
}
