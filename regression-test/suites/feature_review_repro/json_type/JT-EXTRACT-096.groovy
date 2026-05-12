// JT-EXTRACT-096: type string
suite("repro_jt_extract_096") {
    def r = sql "SELECT jsonb_type(CAST('{\"a\":\"hi\"}' AS JSONB), '\$.a')"
    assertEquals("string", r[0][0].toString(), "JT-EXTRACT-096; observed=${r}")
}
