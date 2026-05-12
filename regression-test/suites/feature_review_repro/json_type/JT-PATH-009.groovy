// JT-PATH-009: $.["key with space"]
suite("repro_jt_path_009") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a b\":1}' AS JSONB), '\$.\"a b\"')"
    assertEquals("1", r[0][0].toString(), "JT-PATH-009; observed=${r}")
}
