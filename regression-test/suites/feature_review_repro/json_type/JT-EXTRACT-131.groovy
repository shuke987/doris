// JT-EXTRACT-131: 多 path 含 wildcard + 普通 path
suite("repro_jt_extract_131") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1,\"b\":{\"c\":2,\"d\":3}}' AS JSONB), '\$.a', '\$.b.*')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1"), "JT-EXTRACT-131; observed=${r}")
}
