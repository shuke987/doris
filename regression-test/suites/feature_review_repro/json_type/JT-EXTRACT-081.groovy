// JT-EXTRACT-081: exists_path 不存在
suite("repro_jt_extract_081") {
    def r = sql "SELECT jsonb_exists_path(CAST('{\"a\":1}' AS JSONB), '\$.b')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "0" || v == "false", "JT-EXTRACT-081; observed=${r}")
}
