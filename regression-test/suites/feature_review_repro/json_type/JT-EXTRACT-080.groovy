// JT-EXTRACT-080: exists_path 存在
suite("repro_jt_extract_080") {
    def r = sql "SELECT jsonb_exists_path(CAST('{\"a\":1}' AS JSONB), '\$.a')"
    String v = r[0][0].toString().toLowerCase()
    assertTrue(v == "1" || v == "true", "JT-EXTRACT-080; observed=${r}")
}
