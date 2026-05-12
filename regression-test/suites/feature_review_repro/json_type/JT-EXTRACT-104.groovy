// JT-EXTRACT-104: jsonb_keys 顶层 object
suite("repro_jt_extract_104") {
    def r = sql "SELECT jsonb_keys(CAST('{\"a\":1,\"b\":2}' AS JSONB))"
    String v = r[0][0].toString()
    assertTrue(v.contains("a") && v.contains("b"), "JT-EXTRACT-104; observed=${r}")
}
