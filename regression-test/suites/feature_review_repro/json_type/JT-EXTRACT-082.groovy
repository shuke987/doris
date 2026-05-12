// JT-EXTRACT-082: exists_path 值是 null（key 存在）
suite("repro_jt_extract_082") {
    def r = sql "SELECT jsonb_exists_path(CAST('{\"a\":null}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-EXTRACT-082; observed=${r}")
}
