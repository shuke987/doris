// JT-EXTRACT-022: path 含中文 key
suite("repro_jt_extract_022") {
    def r = sql "SELECT jsonb_extract(CAST('{\"中文\":1}' AS JSONB), '\$.中文')"
    assertEquals("1", r[0][0].toString(), "JT-EXTRACT-022; observed=${r}")
}
