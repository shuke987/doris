// JT-PATH-010: $.["key.with.dot"]
suite("repro_jt_path_010") {
    def r = sql "SELECT jsonb_extract(CAST('{\"key.with.dot\":1}' AS JSONB), '\$.\"key.with.dot\"')"
    assertEquals("1", r[0][0].toString(), "JT-PATH-010; observed=${r}")
}
