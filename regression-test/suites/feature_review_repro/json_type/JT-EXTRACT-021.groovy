// JT-EXTRACT-021: $ root path
suite("repro_jt_extract_021") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$')"
    assertEquals("{\"a\":1}", r[0][0].toString(), "JT-EXTRACT-021; observed=${r}")
}
