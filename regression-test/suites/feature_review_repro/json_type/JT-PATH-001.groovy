// JT-PATH-001: $ root
suite("repro_jt_path_001") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1}' AS JSONB), '\$')"
    assertEquals("{\"a\":1}", r[0][0].toString(), "JT-PATH-001; observed=${r}")
}
