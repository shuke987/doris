// JT-EXTRACT-004: $.* wildcard
suite("repro_jt_extract_004") {
    def r = sql "SELECT jsonb_extract(CAST('{\"a\":1,\"b\":2}' AS JSONB), '\$.*')"
    String v = r[0][0].toString()
    assertTrue(v.contains("1") && v.contains("2"),
        "JT-EXTRACT-004; observed=${r}")
}
